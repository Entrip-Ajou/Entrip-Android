package ajou.paran.entrip.di

import ajou.paran.entrip.repository.Impl.*
import ajou.paran.entrip.repository.network.*
import ajou.paran.entrip.repository.network.api.*
import ajou.paran.entrip.repository.network.VoteRemoteSource
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.repository.room.AppDatabase
import ajou.paran.entrip.repository.room.plan.dao.UserDao
import ajou.paran.entrip.util.EntripV1
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
    fun providePlanRemoteApi(@EntripV1 retrofit: Retrofit) : PlanApi
    = retrofit.create(PlanApi::class.java)

    @Provides
    @Singleton
    fun provideUserRemoteApi(@EntripV1 retrofit: Retrofit) : UserApi
    = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideUserV2RemoteApi(@EntripV2 retrofit: Retrofit) : UserAPIV2
    = retrofit.create(UserAPIV2::class.java)

    @Provides
    @Singleton
    fun provideTokenRemoteApi(@EntripV2 retrofit: Retrofit) : TokenApi
    = retrofit.create(TokenApi::class.java)

    @Provides
    @Singleton
    fun provideFcmRemoteApi(@FCM retrofit: Retrofit) : FcmApi
    = retrofit.create(FcmApi::class.java)

    @Provides
    @Singleton
    fun provideKakaoRemoteApi(@KakaoMap retrofit: Retrofit) : MapApi
    = retrofit.create(MapApi::class.java)

    @Provides
    @Singleton
    fun provideCommentRemoteApi(@EntripV1 retrofit: Retrofit) : CommentApi
    = retrofit.create(CommentApi::class.java)

    @Provides
    @Singleton
    fun provideNoticeRemoteApi(@EntripV1 retrofit: Retrofit) : NoticeApi
    = retrofit.create(NoticeApi::class.java)

    @Provides
    @Singleton
    fun provideVoteRemoteApi(@EntripV1 retrofit: Retrofit) : VoteApi
    = retrofit.create(VoteApi::class.java)

    @Provides
    @Singleton
    fun provideCommunityApi(@EntripV1 retrofit: Retrofit): CommunityApi
    = retrofit.create(CommunityApi::class.java)

    @Provides
    @Singleton
    fun providePlanRemoteSource(
        planApi: PlanApi,
        tokenApi: TokenApi,
        sharedPreferences: SharedPreferences
    ) : PlanRemoteSource = PlanRemoteSource(
        planApi = planApi,
        tokenApi = tokenApi,
        sharedPreferences = sharedPreferences
    )

    @Provides
    @Singleton
    fun providePlannerRemoteSource(
        planApi: PlanApi,
        tokenApi: TokenApi,
        sharedPreferences: SharedPreferences
    ) : PlannerRemoteSource = PlannerRemoteSource(
        planApi = planApi,
        tokenApi = tokenApi,
        sharedPreferences = sharedPreferences
    )

    @Provides
    @Singleton
    fun provideUserRemoteSource(
        userApi: UserApi,
        userAPIV2: UserAPIV2,
        tokenApi: TokenApi,
        sharedPreferences: SharedPreferences
    ) : UserRemoteSource = UserRemoteSource(
        userApi = userApi,
        userAPIV2 = userAPIV2,
        tokenApi = tokenApi,
        sharedPreferences = sharedPreferences
    )

    @Provides
    @Singleton
    fun provideMapRemoteSource(mapApi : MapApi) : MapRemoteSource{
        return MapRemoteSource(mapApi)
    }

    @Provides
    @Singleton
    fun provideCommentRemoteSource(
        commentApi: CommentApi,
        tokenApi: TokenApi,
        sharedPreferences: SharedPreferences
    ) : CommentRemoteSource = CommentRemoteSource(
        commentApi = commentApi,
        tokenApi = tokenApi,
        sharedPreferences = sharedPreferences
    )

    @Provides
    @Singleton
    fun provideNoticeRemoteSource(
        noticeApi: NoticeApi,
        tokenApi: TokenApi,
        sharedPreferences: SharedPreferences
    ) : NoticeRemoteSource = NoticeRemoteSource(
        noticeApi = noticeApi,
        tokenApi = tokenApi,
        sharedPreferences = sharedPreferences
    )

    @Provides
    @Singleton
    fun provideVoteRemoteSource(
        voteApi: VoteApi,
        tokenApi: TokenApi,
        sharedPreferences: SharedPreferences
    ) : VoteRemoteSource = VoteRemoteSource(
        voteApi = voteApi,
        tokenApi = tokenApi,
        sharedPreferences = sharedPreferences
    )

    @Provides
    @Singleton
    fun provideCommunityRemoteSource(
        communityApi: CommunityApi,
        tokenApi: TokenApi,
        sharedPreferences: SharedPreferences
    ): CommunityRemoteSource = CommunityRemoteSource(
        communityApi = communityApi,
        tokenApi = tokenApi,
        sharedPreferences = sharedPreferences
    )

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
    fun provideUserRepository(
        userRemoteSource: UserRemoteSource,
        sharedPreferences: SharedPreferences
    ) : UserRepository = UserRepositoryImpl(userRemoteSource, sharedPreferences)

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

}