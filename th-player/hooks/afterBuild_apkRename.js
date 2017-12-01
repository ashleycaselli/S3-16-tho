module.exports = function (ctx) {
    // make sure android platform is part of build
    if (ctx.opts.platforms.indexOf('android') < 0) {
        return;
    }
    var fs = ctx.requireCordovaModule('fs'),
        path = ctx.requireCordovaModule('path'),
        deferral = ctx.requireCordovaModule('q').defer();

    var platformRoot = path.join(ctx.opts.projectRoot, 'platforms/android');
    var apkFileLocation = path.join(platformRoot, 'build/outputs/apk/debug/');
    var oldApk = "android-debug.apk";
    var newApk = "th-player.apk";

    fs.renameSync(apkFileLocation + oldApk, apkFileLocation + newApk);
    console.log("APK renamed: " + oldApk + " renamed to " + newApk);

    return deferral.promise;
};