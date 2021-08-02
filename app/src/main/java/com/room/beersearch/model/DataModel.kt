package com.room.beersearch.model

sealed class DataModel(val type: DataType) {
    data class Data(val value: BeerSearch): DataModel(DataType.DATA)
    data class Header(val title: String): DataModel(DataType.HEADER)
    object Separator: DataModel(DataType.SEPARATOR)
}