package info.mrprogrammer.mrpillstracker.Login.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import info.mrprogrammer.mrpillstracker.Login.data.data_source.GetTextFlowDataSource
import info.mrprogrammer.mrpillstracker.Login.data.data_source.localDataSource.GetTextFlowLocalDataSource
import info.mrprogrammer.mrpillstracker.Login.data.repository.GetTextFlowDataRepositoryImpl
import info.mrprogrammer.mrpillstracker.Login.data.repository.LoginRepositoryImpl
import info.mrprogrammer.mrpillstracker.Login.domain.repository.GetTextFlowDataRepository
import info.mrprogrammer.mrpillstracker.Login.domain.repository.LoginRepository
import info.mrprogrammer.mrpillstracker.Login.domain.use_cases.DoLogin
import info.mrprogrammer.mrpillstracker.Login.domain.use_cases.GetTextFlowData
import info.mrprogrammer.mrpillstracker.Login.frame_work.GoogleLogin


@Module
@InstallIn(ViewModelComponent::class)
class LoginModule {

    @Provides
    @ViewModelScoped
    fun getTextFlowDataSource(): GetTextFlowDataSource {
        return GetTextFlowLocalDataSource()
    }
    @Provides
    @ViewModelScoped
    fun getTextFlowRepository(getTextFlowDataSource: GetTextFlowDataSource): GetTextFlowDataRepository {
        return GetTextFlowDataRepositoryImpl(getTextFlowDataSource)
    }

    @Provides
    @ViewModelScoped
    fun getTextFlowData(getTextFlowDataRepository: GetTextFlowDataRepository): GetTextFlowData {
        return GetTextFlowData(getTextFlowDataRepository)
    }

    @Provides
    @ViewModelScoped
    fun getGoogleLogin(@ApplicationContext context: Context): GoogleLogin {
        return GoogleLogin(context)
    }

    @Provides
    @ViewModelScoped
    fun getLoginRepository(googleLogin: GoogleLogin):LoginRepository {
        return LoginRepositoryImpl(googleLogin)
    }

    @Provides
    @ViewModelScoped
    fun getDoLogin(loginRepository: LoginRepository):DoLogin {
        return DoLogin(loginRepository)
    }
}