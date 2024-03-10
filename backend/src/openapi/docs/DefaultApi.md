# DefaultApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiUserSigninPost**](DefaultApi.md#apiUserSigninPost) | **POST** /api/user/signin | Sign in
[**apiUserSignoutPost**](DefaultApi.md#apiUserSignoutPost) | **POST** /api/user/signout | Sign out
[**apiUserSignupPost**](DefaultApi.md#apiUserSignupPost) | **POST** /api/user/signup | Sign up a new user


<a id="apiUserSigninPost"></a>
# **apiUserSigninPost**
> apiUserSigninPost(apiUserSigninPostRequest)

Sign in

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val apiUserSigninPostRequest : ApiUserSigninPostRequest =  // ApiUserSigninPostRequest | 
try {
    apiInstance.apiUserSigninPost(apiUserSigninPostRequest)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#apiUserSigninPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#apiUserSigninPost")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **apiUserSigninPostRequest** | [**ApiUserSigninPostRequest**](ApiUserSigninPostRequest.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="apiUserSignoutPost"></a>
# **apiUserSignoutPost**
> apiUserSignoutPost()

Sign out

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    apiInstance.apiUserSignoutPost()
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#apiUserSignoutPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#apiUserSignoutPost")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="apiUserSignupPost"></a>
# **apiUserSignupPost**
> apiUserSignupPost(apiUserSignupPostRequest)

Sign up a new user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val apiUserSignupPostRequest : ApiUserSignupPostRequest =  // ApiUserSignupPostRequest | 
try {
    apiInstance.apiUserSignupPost(apiUserSignupPostRequest)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#apiUserSignupPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#apiUserSignupPost")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **apiUserSignupPostRequest** | [**ApiUserSignupPostRequest**](ApiUserSignupPostRequest.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

