package info.mrprogrammer.mrpillstracker.core.data.data_soruce.save_data

import info.mrprogrammer.mrpillstracker.core.domain.model.SaveDataModel

interface SavaDataDataSource {
    suspend fun saveData(data: SaveDataModel): Boolean
}