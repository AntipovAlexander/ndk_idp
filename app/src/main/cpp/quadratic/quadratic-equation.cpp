#include <jni.h>
#include <string>
#include <cmath>
#include <android/log.h>

#define TAG "CPP_QUADRATIC_SOLVER"

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,    TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,     TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,     TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,    TAG, __VA_ARGS__)

using namespace std;
extern "C" JNIEXPORT jdoubleArray JNICALL
Java_com_antipov_ndk_1idp_MainActivity_quadraticEquation(
        JNIEnv *env,
        jobject /* this */,
        jint a,
        jint b,
        jint c) {

    LOGI("Received params");
    LOGI("A = %d", a);
    LOGI("B = %d", b);
    LOGI("C = %d", c);
    LOGI("Computing ...");

    if ((b * b - 4 * a * c) >= 0) {
        // calculating firs and second roots
        double first = (-1 * b + sqrt(b * b - 4 * a * c)) / (2 * a);
        double second = (-1 * b - sqrt(b * b - 4 * a * c)) / (2 * a);
        // preparing array for roots
        double values[2] = {first, second};
        jdoubleArray javaArray = env->NewDoubleArray(2);
        env->SetDoubleArrayRegion(javaArray, 0, 2, values);
        // return roots
        LOGI("First root is = %f", values[0]);
        LOGI("Second root is = %f", values[1]);
        return javaArray;
    } else {
        // id D < 0 throw exception to java
        jclass exception = env->FindClass("java/lang/Exception");
        LOGE("D < 0, abort.");
        return reinterpret_cast<jdoubleArray>(env->ThrowNew(exception, "D < 0, abort."));
    }
}