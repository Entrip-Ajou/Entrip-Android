package ajou.paran.entrip.di

import ajou.paran.entrip.repository.Impl.UserRepository
import ajou.paran.entrip.repository.usecase.IsExistNicknameUseCase
import ajou.paran.entrip.repository.usecase.IsExistUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideIsExistUserUseCase(userRepository: UserRepository): IsExistUserUseCase{
        return IsExistUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideIsExistNicknameUseCase(userRepository: UserRepository): IsExistNicknameUseCase{
        return IsExistNicknameUseCase(userRepository)
    }
}