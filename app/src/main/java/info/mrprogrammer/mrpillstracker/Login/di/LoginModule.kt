package info.mrprogrammer.mrpillstracker.Login.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import info.mrprogrammer.mrpillstracker.Login.data.data_source.GetTextFlowDataSource
import info.mrprogrammer.mrpillstracker.Login.data.data_source.localDataSource.GetTextFlowLocalDataSource
import info.mrprogrammer.mrpillstracker.Login.data.repository.GetTextFlowDataRepositoryImpl
import info.mrprogrammer.mrpillstracker.Login.domain.repository.GetTextFlowDataRepository
import info.mrprogrammer.mrpillstracker.Login.domain.use_cases.GetTextFlowData


@Module
@InstallIn(ViewModelComponent::class)
class LoginModule {

    @Provides
    fun getTextFlowDataSource(): GetTextFlowDataSource {
        return GetTextFlowLocalDataSource()
    }
    @Provides
    fun getTextFlowRepository(getTextFlowDataSource: GetTextFlowDataSource): GetTextFlowDataRepository {
        return GetTextFlowDataRepositoryImpl(getTextFlowDataSource)
    }

    @Provides
    fun getTextFlowData(getTextFlowDataRepository: GetTextFlowDataRepository): GetTextFlowData {
        return GetTextFlowData(getTextFlowDataRepository)
    }
}