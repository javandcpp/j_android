#include "com_yzk_clibapp_JniUtils.h"

JNIEXPORT jstring JNICALL Java_com_yzk_clibapp_JniUtils_test
        (JNIEnv *env, jobject jobj) {
    return env->NewStringUTF("Hello World!");
}
