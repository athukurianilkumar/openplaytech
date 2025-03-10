package com.openplaytech.openplay.model.mappers

import com.openplaytech.openplay.database.MovieDbDatabase
import com.openplaytech.openplay.database.dao.MovieDbTable
import com.openplaytech.openplay.database.dao.MovieDbTableDao
import com.openplaytech.openplay.model.parsers.search.SearchItem
import com.openplaytech.openplay.model.parsers.search.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HomeDataModelMapperImplTest {

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private lateinit var mapper: HomeDataModelMapperImpl

    @Mock
    lateinit var mockMovieDbDatabase: MovieDbDatabase

    @Mock
    lateinit var mockMovieDbTableDao: MovieDbTableDao

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        mapper = HomeDataModelMapperImpl(mockMovieDbDatabase)
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given empty list When toHomeDataModelFromTable is called Then return empty list`() {
        val emptyList = listOf<MovieDbTable>()

        val result = mapper.toHomeDataModelFromTable(emptyList)

        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `Given empty list When toHomeDataModelFromResponse is called Then return empty list`() =
        runTest {
            val response = SearchResponse()

            val result = mapper.toHomeDataModelFromResponse(response)

            Assert.assertEquals(0, result.size)
        }

    @Test
    fun `Given a fulfilled response When toHomeDataModelFromResponse is called Then verify the correct mapping`() =
        runTest {
            val response = SearchResponse(
                searchResultsList = arrayListOf(
                    SearchItem(
                        id = 1,
                        title = "title",
                        overview = "overview"
                    )
                )
            )


            Mockito.`when`(mockMovieDbDatabase.movieDbTableDao()).thenReturn(mockMovieDbTableDao)

            val result = mapper.toHomeDataModelFromResponse(response)

            Assert.assertEquals(1, result.size)
            Assert.assertEquals(1, result.first().id)
            Assert.assertEquals("title", result.first().title)
            Assert.assertEquals("overview", result.first().summary)
        }
}
