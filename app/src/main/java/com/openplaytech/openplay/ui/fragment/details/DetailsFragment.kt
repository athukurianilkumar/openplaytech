package com.openplaytech.openplay.ui.fragment.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import dagger.hilt.android.AndroidEntryPoint
import com.openplaytech.openplay.common.ActivityResult
import com.openplaytech.openplay.common.BUNDLE
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.mvvm.viewModel.details.DetailsViewModel
import com.openplaytech.openplay.ui.base.BaseFragment
import com.openplaytech.openplay.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.databinding.ActivityDetailsBinding

@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsViewModel>() {

    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var binding: ActivityDetailsBinding

    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressed()
        }
    }

    private fun onBackPressed() {
        if (viewModel?.hasUserChangeFavourite == true && viewModel?.homeDataModel != null) {
            val bundle = Bundle().apply {
                putParcelable(BUNDLE.MOVIE_DETAILS, viewModel?.homeDataModel)
            }
            setFragmentResult(ActivityResult.DETAILS, bundle)
        }
        findNavController().navigateUp()
    }


    private fun initViewModel() {
        viewModel = detailsViewModel
        viewModel?.setUIModel(args.homeDataModel) ?: kotlin.run {
            binding.emptyLayout?.root?.visibility = View.VISIBLE
        }
        initViewModelState(binding.loadingLayout, binding.emptyLayout)
        viewModel?.getDetailsList()?.observe(viewLifecycleOwner, { resultList ->
            resultList?.let {
                updateUi(it)
            } ?: run {
                binding.emptyLayout?.root?.visibility = View.VISIBLE
            }
        })

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun updateUi(homeDataModel: HomeDataModel) {
        binding.apply {

            thumbnailImageView.setContent {
                MovieDBTheme {
                    Image(
                            painter = getImage(homeDataModel.thumbnail),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                    )
                }
            }

            toolbarLayout.toolbarTitleTextView.text = homeDataModel.title
            summaryTextView.setContent {
                MovieDBTheme {
                    ComposeText(homeDataModel.summary ?: "-", 6, Modifier.padding(bottom = 2.dp))
                }
            }
            genreTitleTextView.setContent {
                MovieDBTheme {
                    ComposeText(
                            text = stringResource(R.string.details_genre),
                            modifier = Modifier.padding(bottom = 2.dp, end = 4.dp))
                }
            }

            genreTextView.setContent {
                MovieDBTheme {
                    ComposeText(
                            text = homeDataModel.genresName ?: "-",
                            maxLines = 2,
                            modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun getImage(thumbnail: String?): Painter {
        return if (thumbnail != null && thumbnail.contains("null")) {
            painterResource(id = R.mipmap.ic_launcher_round)
        } else {
            rememberImagePainter(thumbnail)
        }
    }

    @Composable
    private fun ComposeText(text: String = "-",
                            maxLines: Int = 1,
                            modifier: Modifier) {
        Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
        )
    }
}
