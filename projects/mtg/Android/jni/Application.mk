APP_PROJECT_PATH := $(call my-dir)/..
APP_CPPFLAGS += -frtti -fexceptions
APP_ABI := armeabi-v7a
#APP_ABI := x86 # mainly for emulators
APP_STL := c++_static
APP_MODULES := libpng libjpeg main SDL

#APP_OPTIM is 'release' by default
APP_OPTIM := release
