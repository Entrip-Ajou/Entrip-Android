package ajou.paran.entrip.di

import ajou.paran.entrip.repository.Impl.*
import ajou.paran.entrip.repository.network.*
import ajou.paran.entrip.repository.network.api.*
import ajou.paran.entrip.repository.network.VoteRemoteSource
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.repository.room.AppDatabase
import ajou.paran.entrip.repository.room.plan.dao.UserDao
import ajou.paran.entrip.util.Entrip
import ajou.paran.entrip.util.EntripV2
import ajou.paran.entrip.util.FCM
import ajou.paran.entrip.util.KakaoMap
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePlanRemoteApi(@Entrip retrofit: Retrofit) : PlanApi {
        return retrofit.create(PlanApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRemoteApi(@Entrip retrofit: Retrofit) : UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFcmRemoteApi(@FCM retrofit: Retrofit) : FcmApi {
        return retrofit.create(FcmApi::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoRemoteApi(@KakaoMap retrofit: Retrofit) : MapApi {
        return retrofit.create(MapApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentRemoteApi(@Entrip retrofit: Retrofit) : CommentApi {
        return retrofit.create(CommentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNoticeRemoteApi(@Entrip retrofit: Retrofit) : NoticeApi {
        return retrofit.create(NoticeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVoteRemoteApi(@Entrip retrofit: Retrofit) : VoteApi {
        return retrofit.create(VoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommunityApi(@Entrip retrofit: Retrofit): CommunityApi = retrofit.create(CommunityApi::class.java)

    @Provides
    @Singleton
    fun provideUserV2Api(
        @EntripV2 retrofit: Retrofit
    ): UserAPIV2 = retrofit.create(UserAPIV2::class.java)

    @Provides
    @Singleton
    fun providePlanRemoteSource(planApi: PlanApi) : PlanRemoteSource{
        return PlanRemoteSource(planApi)
    }

    @Provides
    @Singleton
    fun providePlannerRemoteSource(planApi: PlanApi) : PlannerRemoteSource{
        return PlannerRemoteSource(planApi)
    }

    @Provides
    @Singleton
    fun provideUserRemoteSource(userApi: UserApi) : UserRemoteSource {
        return UserRemoteSource(userApi)
    }

    @Provides
    @Singleton
    fun provideMapRemoteSource(mapApi : MapApi) : MapRemoteSource{
        return MapRemoteSource(mapApi)
    }

    @Provides
    @Singleton
    fun provideCommentRemoteSource(commentApi: CommentApi) : CommentRemoteSource{
        return CommentRemoteSource(commentApi)
    }

    @Provides
    @Singleton
    fun provideNoticeRemoteSource(noticeApi: NoticeApi) : NoticeRemoteSource {
        return NoticeRemoteSource(noticeApi)
    }

    @Provides
    @Singleton
    fun provideVoteRemoteSource(voteApi: VoteApi) : VoteRemoteSource {
        return VoteRemoteSource(voteApi)
    }

    @Provides
    @Singleton
    fun provideCommunityRemoteSource(communityApi: CommunityApi): CommunityRemoteSource = CommunityRemoteSource(communityApi)

    @Provides
    @Singleton
    fun provideUserV2RemoteSource(
        userAPIV2: UserAPIV2
    ): UserV2RemoteSource = UserV2RemoteSource(userAPIV2 = userAPIV2)

    @Provides
    @Singleton
    fun providePlanDao(appDatabase: AppDatabase) : PlanDao {
        return appDatabase.planDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase) : UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun providePlanRepository(planRemoteSource: PlanRemoteSource, planDao: PlanDao) : PlanRepository{
        return PlanRepositoryImpl(planRemoteSource, planDao)
    }

    @Provides
    @Singleton
    fun providePlannerRepository(plannerRemoteSource: PlannerRemoteSource, planDao: PlanDao, userDao: UserDao) : PlannerRepository{
        return PlannerRepositoryImpl(plannerRemoteSource, planDao, userDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userRemoteSource: UserRemoteSource, plannerRemoteSource: PlannerRemoteSource ,  planDao: PlanDao) : UserRepository{
        return UserRepositoryImpl(userRemoteSource, plannerRemoteSource, planDao)
    }

    @Provides
    @Singleton
    fun provideUserAddRepository(userAddRemoteSource: UserAddRemoteSource, userDao: UserDao) : UserAddRepository{
        return UserAddRepositoryImpl(userAddRemoteSource, userDao)
    }

    @Provides
    @Singleton
    fun provideRecommendRepository() : RecommendRepository{
        return RecommendRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideCommentRepository(commentRemoteSource: CommentRemoteSource, planDao : PlanDao) : CommentRepository{
        return CommentRepositoryImpl(commentRemoteSource, planDao)
    }

    @Provides
    @Singleton
    fun provideNoticeRepository(noticeRemoteSource: NoticeRemoteSource) : NoticeRepository {
        return NoticeRepositoryImpl(noticeRemoteSource)
    }

    @Provides
    @Singleton
    fun provideVoteRepository(voteRemoteSource: VoteRemoteSource) : VoteRepository {
        return VoteRepositoryImpl(voteRemoteSource)
    }

    @Provides
    @Singleton
    fun provideCommunityRepository(communityRemoteSource: CommunityRemoteSource): CommunityRepository = CommunityRepositoryImpl(communityRemoteSource)

    @Provides
    @Singleton
    fun provideUserV2Repository(
        userV2RemoteSource: UserV2RemoteSource,
        sharedPreferences: SharedPreferences
    ): UserV2Repository = UserV2RepositoryImpl(userV2RemoteSource, sharedPreferences)

}