package com.kwangeonkim.thoth.domain.mapper

interface EntityMapper<UiModel, DataModel> {

    fun toUiModel(dataModel: DataModel): UiModel

    fun toDataModel(uiModel: UiModel): DataModel
}