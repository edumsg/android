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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.GsonConverter;

public class ApiRouter {
	private static final String API_BASE_URL = "http://192.168.1.14:8080";

	private static PublicApiRoutes publicRouter;
	private static PrivateApiRoutes privateRouter;

	public static PublicApiRoutes withoutToken() {
		if (publicRouter == null) {
			publicRouter = buildRouter(PublicApiRoutes.class,  new RequestInterceptor(){
				@Override
				public void intercept(RequestInterceptor.RequestFacade request) {
					request.addHeader("Content-Type", "application/json");
					request.addHeader("Connection", "keep-alive");
				}
			});
		}

		return publicRouter;
	}

	public static PrivateApiRoutes withToken(String token) {
		//if (privateRouter == null) {
			privateRouter = buildRouter(PrivateApiRoutes.class, new PrivateApiInterceptor(token));
		//}

		return privateRouter;
	}

	private static <T> T buildRouter(Class<T> T, RequestInterceptor requestInterceptor) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		gsonBuilder.registerTypeAdapter(Date.class, new ApiDateTypeAdapter());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();

		RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder();
		restAdapterBuilder.setEndpoint(API_BASE_URL);
		if (requestInterceptor != null) {
			restAdapterBuilder.setRequestInterceptor(requestInterceptor);
		}
		restAdapterBuilder.setConverter(new GsonConverter(gson));

		RestAdapter restAdapter = restAdapterBuilder.build();
		restAdapter.setLogLevel(LogLevel.FULL);
		return restAdapter.create(T);
	}
}
