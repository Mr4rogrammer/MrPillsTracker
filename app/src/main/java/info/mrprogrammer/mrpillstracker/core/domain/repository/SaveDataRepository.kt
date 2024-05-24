package info.mrprogrammer.mrpillstracker.core.domain.repository

import info.mrprogrammer.mrpillstracker.core.domain.model.SaveDataModel

interface SaveDataRepository {
    suspend fun saveData(data: SaveDataModel): Boolean
}