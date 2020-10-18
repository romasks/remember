package com.remember.app.data.dataFlow.network

import com.remember.app.data.models.ChatMessages
import com.remember.app.data.models.SuccessSendMessage
import com.remember.app.data.models.ChatsModel
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface Api {

    @GET("chat")
    fun getChatList(@Header("Authorization") token : String,@Query("limit") limit: Int, @Query("offset") offset: Int) : Single<ChatsModel>


    @GET("chat/{id}")
    fun getChatMessage(@Header("Authorization") token : String,@Path("id") id : Int, @Query("limit") limit: Int, @Query("offset") offset: Int) : Single<ChatMessages>

    @FormUrlEncoded
    @POST("chat/{id}")
    fun sendMessage(@Header("Authorization") token : String,@Path("id") id : String, @Field("text") body : String): Single<SuccessSendMessage>

    @DELETE("chat/{id}")
    fun deleteChat(@Header("Authorization") token : String,@Path("id") id : Int): Single<SuccessSendMessage>

    @GET("chat/{id}/info")
    fun getChatInfo(@Header("Authorization") token : String,@Path("id") id : Int): Single<SuccessSendMessage>

    @DELETE("chat/{id}/message/{message_id}")
    fun deleteMessage(@Header("Authorization") token : String,@Path("id") id : Int, @Path("message_id") messageId : Int, @Query("mutual") deleteAll : Boolean) : Single<SuccessSendMessage>

    @PUT("chat/{id}/message/{message_id}")
    fun editMessage(@Header("Authorization") token : String,@Path("id") id : Int, @Path("message_id") messageId : Int,@Body body : RequestBody): Single<SuccessSendMessage>

    @PUT("chat/{id}/message/{message_id}/read")
    fun readMark(@Header("Authorization") token : String,@Path("id") id : Int, @Path("message_id") messageId : Int): Single<SuccessSendMessage>


//    @POST("auth/registration")
//    fun registration(@Body req: RegistrationRequest): Single<RegistrationResponse>
//
//    @POST("auth/forgot-account")
//    fun forgot(@Body req: ForgotRequest): Single<RegistrationResponse>
//
//    @GET("mobile-user/info")
//    fun getUser(@Header("Authorization") token : String) : Single<UserResponse>
//
//
//    @PATCH("mobile-user/change-info")
//    fun editProfile(@Header("Authorization")token : String, @Body body : EditProfileRequest) : Single<EditProfileResponse>
//
//
//    @POST("mobile-user/upload-photo")
//    @Multipart
//    fun uploadImage(@Header("Authorization")token : String, @Part photo : MultipartBody.Part) : Single<EditProfileResponse>
//
//
//    @GET("mobile-user/get-notice")
//    fun getNotification(@Header("Authorization") token : String) : Single<NotificationResponse>
//
//    @PATCH("mobile-user/update-notice")
//    fun updateNotification(@Header("Authorization") token : String, @Body body : NotificationRequest) : Single<NotificationResponse>
//
//
//
//    @GET("mobile-user/get-privacy")
//    fun getPrivacy(@Header("Authorization") token : String) : Single<PrivacyResponse>
//
//    @PATCH("mobile-user/update-privacy")
//    fun updatePrivacy(@Header("Authorization") token : String, @Body body : PrivacyRequest) : Single<PrivacyResponse>
//
//
//    @GET("mobile-user/block-contacts")
//    fun getBlockedList(@Header("Authorization") token : String) : Single<BlockedResponse>
//
//    @POST("mobile-user/add-block-contact")
//    fun addToBlockedList(@Header("Authorization") token : String, @Body id : Array<String>) : Single<BlockedResponse>
//
//    @DELETE("mobile-user/delete-block-contact/{id}")
//    fun deleteFromBlockedList(@Header("Authorization") token : String, @Path("id")  id : String) : Single<BlockedResponse>
//
//    @DELETE("mobile-user/delete-photo")
//    fun deleteCurrentPhoto(@Header("Authorization") token : String) : Single<DeleteCurrentPhoto>
//
//    @GET("mobile-user/personal-account")
//    fun getPersonalAccount(@Header("Authorization") token : String) : Single<PersonalResponse>
//
//    @GET("news/list/{id}")
//    fun getNews(@Header("Authorization") token : String, @Path("id") id: String) : Single<NewsDetailsResponse>
//
//    @GET("news/id/{id}")
//    fun getNewsDetails(@Header("Authorization") token : String, @Path("id") id: String) : Single<NewsDetailsResponse>
//
//    @GET("interrogation/list/{id}")
//    fun getInterviews(@Header("Authorization") token : String, @Path("id") personalID: String) : Single<InterviewsResponse>
//
//    @GET("interrogation/id/{id}/{personal_account_id}")
//    fun getInterviewDetails(@Header("Authorization") token : String,@Path("id") interviewID: String, @Path("personal_account_id") personalID: String) : Single<InterviewDetails>
//
//    @POST("interrogation/vote/{id}/{personal_account_id}")
//    fun sendInterviewAnswer(@Header("Authorization") token : String,@Path("id") interviewID: String, @Path("personal_account_id") personalID: String,  @Body id : Array<InterviewRequest>): Single<InterviewEndResponse>
//
//    @GET("interrogation/end-list/{id}")
//    fun getEndedInterview(@Header("Authorization") token : String, @Path("id") personalID: String) : Single<EndedInterviewsResponse>
//
//    @POST("meters-data/add-counter/{id}")
//    fun addCounter(@Header("Authorization") token : String, @Path("id") personalID: String, @Body req: AddCounterRequest) : Single<AddCounterModel>
//
//
//    @POST("meters-data/transmit-meter/{id}")
//    fun addCounterScore(@Header("Authorization") token : String, @Path("id") personalID: String, @Body req: AddCounterScoreRequest) : Single<AddCounterScoreResponse>
//
//
//    @GET("meters-data/history/{id}")
//    fun getCounterScoreHistory(@Header("Authorization") token : String, @Path("id") personalID: String) : Single<CounterHistoryResponse>
//
//    @GET("meters-data/counters/{id}")
//    fun getCounters(@Header("Authorization") token : String, @Path("id") personalID: String) : Single<AddCounterResponse>
//
//    @GET("services/categories/{id}")
//    fun getServicesCategories(@Header("Authorization") token : String, @Path("id") personalID: String) : Single<ServicesCategoriesResponse>
//
//    @GET("services/service-category/{id}")
//    fun getCategoryServices(@Header("Authorization") token : String, @Path("id") categoryID: String) : Single<CategoryServicesResponse>
//
//    @POST("services/request/{service_id}/{personal_account_id}")
//    @Multipart
//    fun sendBidWithoutPhoto(@Header("Authorization") token : String, @Path("service_id") serviceID: String, @Path("personal_account_id") personalID: String, @Part("description") description : RequestBody) : Single<SendBidResponse>
//
//    @POST("services/request/{service_id}/{personal_account_id}")
//    @Multipart
//    fun sendBidOnePhoto(@Header("Authorization") token : String, @Path("service_id") serviceID: String, @Path("personal_account_id") personalID: String, @Part photo : MultipartBody.Part, @Part("description") description : RequestBody) : Single<SendBidResponse>
//
//    @POST("services/request/{id}/{personal_account_id}")
//    @Multipart
//    fun sendBidTwoPhoto(@Header("Authorization") token : String, @Path("id") categoryID: String, @Path("personal_account_id") personalID: String, @Part photo : MultipartBody.Part,@Part photo2 : MultipartBody.Part, @Part("description") description : RequestBody) : Single<SendBidResponse>
//
//    @POST("services/request/{id}/{personal_account_id}")
//    @Multipart
//    fun sendBidThreePhoto(@Header("Authorization") token : String, @Path("id") categoryID: String, @Path("personal_account_id") personalID: String, @Part photo : MultipartBody.Part, @Part photo2 : MultipartBody.Part,@Part photo3 : MultipartBody.Part,@Part("description") description : RequestBody) : Single<SendBidResponse>
//
//
//    @GET("services/request-list/{id}")
//    fun getBids(@Header("Authorization") token : String, @Path("id") personalID: String) : Single<MyBidsResponse>
//
//    @POST("services/rating/{id}/{personal_account_id}")
//    fun rateService(@Header("Authorization") token : String, @Path("id") servicesID: String, @Path("personal_account_id") personalID: String, @Body body : RateServiceRequest) : Single<RateServiceResponse>

}