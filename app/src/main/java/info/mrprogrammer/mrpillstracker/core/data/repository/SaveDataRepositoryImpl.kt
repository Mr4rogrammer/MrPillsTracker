package info.mrprogrammer.mrpillstracker.core.data.repository

import info.mrprogrammer.mrpillstracker.core.data.data_soruce.save_data.SaveDataDataSource
import info.mrprogrammer.mrpillstracker.core.domain.repository.SaveDataRepository
import info.mrprogrammer.mrpillstracker.core.domain.model.SaveDataModel
import javax.inject.Inject

class SaveDataRepositoryImpl @Inject constructor(private val saveDataDataSource: SaveDataDataSource): SaveDataRepository {
    override suspend fun saveData(data: SaveDataModel): Boolean {
        return saveDataDataSource.saveData(data)
    }
}