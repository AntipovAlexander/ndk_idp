#include <jni.h>
#include <string>
#include <cmath>

using namespace std;
extern "C" JNIEXPORT jdoubleArray JNICALL
Java_com_antipov_ndk_1idp_MainActivity_quadraticEquation(
        JNIEnv *env,
        jobject /* this */,
        jint a,
        jint b,
        jint c) {

    if ((b * b - 4 * a * c) >= 0) {
        // calculating firs and second roots
        double first = (-1 * b + sqrt(b * b - 4 * a * c)) / (2 * a);
        double second = (-1 * b - sqrt(b * b - 4 * a * c)) / (2 * a);
        // preparing array for roots
        double values[2] = {first, second};
        jdoubleArray javaArray = env->NewDoubleArray(2);
        env->SetDoubleArrayRegion(javaArray, 0, 2, values);
        // return roots
        return javaArray;
    } else {
        // id D < 0 throw exception to java
        jclass exception = env->FindClass("java/lang/Exception");
        return reinterpret_cast<jdoubleArray>(env->ThrowNew(exception, "D < 0, abort."));
    }
}