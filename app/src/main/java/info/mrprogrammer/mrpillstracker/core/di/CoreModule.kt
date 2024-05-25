package info.mrprogrammer.mrpillstracker.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_data.GetDataDataSource
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_data.local.GetDataLocalDataSource
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_user_login_details.GetUserLoginDetailsDataSource
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_user_login_details.local.GetUserLoginDetailsLocalDataSource
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.save_data.SaveDataDataSource
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.save_data.remote.SaveDataRemoteDataSource
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.sync_data_from_server.SyncDataDataSource
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.sync_data_from_server.remote.SyncDataRemoteDataSource
import info.mrprogrammer.mrpillstracker.core.data.repository.DataRepositoryImpl
import info.mrprogrammer.mrpillstracker.core.data.repository.GetUserLoginDetailsRepositoryImpl
import info.mrprogrammer.mrpillstracker.core.data.repository.SaveDataRepositoryImpl
import info.mrprogrammer.mrpillstracker.core.data.repository.SyncDataRepositoryImpl
import info.mrprogrammer.mrpillstracker.core.domain.repository.DataRepository
import info.mrprogrammer.mrpillstracker.core.domain.repository.GetUserLoginDetailsRepository
import info.mrprogrammer.mrpillstracker.core.domain.repository.SaveDataRepository
import info.mrprogrammer.mrpillstracker.core.domain.repository.SyncDataRepository
import info.mrprogrammer.mrpillstracker.core.domain.use_case.GetMedicationReminderData
import info.mrprogrammer.mrpillstracker.core.domain.use_case.GetUserLoginDetails
import info.mrprogrammer.mrpillstracker.core.domain.use_case.SaveData
import info.mrprogrammer.mrpillstracker.core.domain.use_case.SyncData
import info.mrprogrammer.mrpillstracker.core.frame_work.LocalDataBase
import info.mrprogrammer.mrpillstracker.core.frame_work.realm.LocalDataBaseImpl

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    fun getSavaDataDataSource(): SaveDataDataSource {
        return SaveDataRemoteDataSource()
    }

    @Provides
    fun getSaveDataRepository(saveDataDataSource: SaveDataDataSource):SaveDataRepository {
        return SaveDataRepositoryImpl(saveDataDataSource)
    }

    @Provides
    fun getSaveData(saveRepository: SaveDataRepository): SaveData {
        return SaveData(saveRepository)
    }



    @Provides
    fun getUserLoginDetailsDataSource():GetUserLoginDetailsDataSource {
        return  GetUserLoginDetailsLocalDataSource()
    }

    @Provides
    fun getUserLoginDetailsRepository(getUserLoginDetailsDataSource: GetUserLoginDetailsDataSource):GetUserLoginDetailsRepository {
        return GetUserLoginDetailsRepositoryImpl(getUserLoginDetailsDataSource)
    }

    @Provides
    fun getUserLoginDetails(getUserLoginDetailsRepository: GetUserLoginDetailsRepository):GetUserLoginDetails {
        return GetUserLoginDetails(getUserLoginDetailsRepository)
    }



    @Provides
    fun getLocalDatabase():LocalDataBase {
        return LocalDataBaseImpl()
    }

    @Provides
    fun getSyncDataDataSource(localDataBase: LocalDataBase):SyncDataDataSource {
        return SyncDataRemoteDataSource(localDataBase)
    }

    @Provides
    fun getSyncDataRepository(syncDataDataSource: SyncDataDataSource):SyncDataRepository {
        return SyncDataRepositoryImpl(syncDataDataSource)
    }

    @Provides
    fun syncData(syncDataRepository: SyncDataRepository): SyncData {
        return SyncData(syncDataRepository)
    }


    @Provides
    fun getDataDataSource(localDataBase: LocalDataBase):GetDataDataSource {
        return GetDataLocalDataSource(localDataBase)
    }

    @Provides
    fun getDataRepository(getDataDataSource: GetDataDataSource):DataRepository {
        return DataRepositoryImpl(getDataDataSource)
    }

    @Provides
    fun getGetMedicationReminderData(dataRepository: DataRepository):GetMedicationReminderData {
        return GetMedicationReminderData(dataRepository)
    }

}