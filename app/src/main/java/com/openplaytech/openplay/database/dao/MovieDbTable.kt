package com.openplaytech.openplay.database.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MovieDbTable")
class MovieDbTable(
        @PrimaryKey
        var id: Int,
        var mediaType: String = "",
        var title: String = "-",
        var summary: String = "-",
        var thumbnail: String = "",
        var releaseDate: String = "-",
        var ratings: String = "-",
        var genresName: String = "-",
        var genre: String = "",
        var dateAdded: Long = 0
)