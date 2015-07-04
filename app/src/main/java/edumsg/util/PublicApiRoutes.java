/*
EduMsg is made available under the OSI-approved MIT license.
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
*/

package edumsg.util;


import edumsg.Model.LoginResponse;
import edumsg.Model.TimeLineResponse;
import edumsg.Tasks.DeleteTweetTask;
import edumsg.Tasks.LoginTask;
import edumsg.Tasks.LogoutTask;
import edumsg.Tasks.NewTweetTask;
import edumsg.Tasks.TimeLineCommandTask;
import edumsg.Tasks.UserTask;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public interface PublicApiRoutes {
	@POST("/")
	void login(@Body LoginTask loginTask, Callback<LoginResponse> callback);

    @POST("/")
    void register(@Body UserTask userTask, Callback<Response> callback);

    @POST("/")
    void logout(@Body LogoutTask logoutTask, Callback<Response> callback);

    @POST("/")
    void tweet(@Body NewTweetTask newTweetTask, Callback<Response> callback);

    @POST("/")
    void getProfile(@Body TimeLineCommandTask timeLineCommandTask, Callback<TimeLineResponse> callback);

    @POST("/")
    void deleteTweet(@Body DeleteTweetTask deleteTweetTask, Callback<Response> callback);
}

