package himasif.ilkom.unej.ac.id.myapplication.Fragments;

import himasif.ilkom.unej.ac.id.myapplication.Notif.MyResponse;
import himasif.ilkom.unej.ac.id.myapplication.Notif.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-type: application/json",
                    "Authorization:key=AAAAtlUT6ds:APA91bGc5KKr1RggPcNts2J2q7naM5aNhIpH1XxttBlPaZgISp89RorJrbCP8GXL7EFXO_M4XAj4CiLLcl2s0pRlsP-xTuFJzOT-6BtAVyG5nkzIQB1IMnxTEfzVG_QtJbhyZ2LALif-"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
