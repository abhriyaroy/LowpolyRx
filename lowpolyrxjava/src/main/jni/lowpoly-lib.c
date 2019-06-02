#include <jni.h>
#include <stddef.h>
#include "lowpoly.h"

JNIEXPORT jintArray JNICALL
Java_com_zebrostudio_lowpolyrxjava_LowPolyRx_getTriangles(JNIEnv *env, jclass type,
                                                          jintArray pixels_,
                                                          jint width, jint height, jint threshold,
                                                          jfloat point_count) {
    jint *pixels = (*env)->GetIntArrayElements(env, pixels_, NULL);
    jint size = (*env)->GetArrayLength(env, pixels_);

    jintArray tmpArray_ = (*env)->NewIntArray(env, size);
    jint *tmpArray = (*env)->GetIntArrayElements(env, tmpArray_, NULL);

    int tmpSize = 0;

    get_triangles(pixels, size, width, height, threshold, point_count, tmpArray, &tmpSize);

    (*env)->ReleaseIntArrayElements(env, pixels_, pixels, 0);

    jintArray result = (*env)->NewIntArray(env, tmpSize);

    (*env)->SetIntArrayRegion(env, result, 0, tmpSize, tmpArray);

    (*env)->ReleaseIntArrayElements(env, tmpArray_, tmpArray, 0);
    return result;
}