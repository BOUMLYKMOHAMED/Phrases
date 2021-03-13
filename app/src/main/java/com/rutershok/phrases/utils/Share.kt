package com.rutershok.phrases.utils

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.rutershok.phrases.R
import com.rutershok.phrases.model.Storage
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object Share {

    @JvmStatic
    fun openPublisherPage(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://search?q=pub:Rutershok")
        context.startActivity(intent)
    }

    @JvmStatic
    fun openAppPage(context: Context, packageName: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    }

    fun launchApp(context: Context, packageName: String) {
        try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            context.startActivity(context.packageManager.getLaunchIntentForPackage(packageName))
        } catch (e: PackageManager.NameNotFoundException) {
            openAppPage(context, packageName)
        }
    }

    @JvmStatic
    fun openInstagram(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/_u/" + Key.RUTERSHOK)
                ).setPackage("com.instagram.android")
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + Key.RUTERSHOK)
                )
            )
        }
    }

    @JvmStatic
    fun openFacebook(context: Context) {
        try {
            context.packageManager.getPackageInfo("com.facebook.katana", 0)
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("fb://page/" + Key.FACEBOOK_ID)
                )
            )
        } catch (e: Exception) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/" + Key.RUTERSHOK)
                )
            )
        }
    }

    @JvmStatic
    fun openTwitter(context: Context) {
        val uri: Uri?
        uri = try {
            context.packageManager.getPackageInfo("com.twitter.android", 0)
            Uri.parse("twitter://user?user_id=" + Key.TWITTER_ID)
        } catch (e: Exception) {
            Uri.parse("https://twitter.com/" + Key.RUTERSHOK)
        }
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    @JvmStatic
    fun contactUs(context: Context) {
        val uri = Uri.parse("mailto:" + Key.EMAIL_ADDRESS)
            .buildUpon()
            .appendQueryParameter("subject", context.getString(R.string.app_name))
            .build()
        context.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SENDTO, uri), context.getString(
                    R.string.send_mail
                )
            )
        )
    }

    @JvmStatic
    fun shareApp(context: Context) {
        val intent = Intent().setAction(Intent.ACTION_SEND).putExtra(
            Intent.EXTRA_TEXT, context.getString(
                R.string.download_this_app
            )
        ).setType("text/plain")
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_with)))
    }

    @JvmStatic
    fun withText(context: Context, phrase: String) {
        if (phrase != "") {
            val phraseText = StringBuilder(phrase)
            if (!Storage.getPremium(context)) {
                phraseText.append(context.getString(R.string.download_this_app))
            }
            context.startActivity(
                Intent.createChooser(
                    Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, phraseText.toString()).setType("text/plain"),
                    context.getString(R.string.share_with)
                )
            )
        }
    }

    @JvmStatic
    fun copyToClipboard(context: Context, phrase: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(
            Key.PHRASE,
            phrase + " " + context.getString(R.string.download_this_app)
        )
        clipboard.setPrimaryClip(clip)
    }

    private fun getBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.height, view.height, Bitmap.Config.ARGB_8888)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(Canvas(bitmap))
        return Bitmap.createScaledBitmap(bitmap, view.width, view.height, true)
    }

    @JvmStatic
    fun withImage(activity: Activity) {
        val bitmap = getBitmap(activity.findViewById(R.id.cl_background))
        Thread {
            try {
                val cachePath = File(activity.cacheDir, "images")
                if (!cachePath.exists()) {
                    cachePath.mkdirs()
                } // don't forget to make the directory
                val outputStream = FileOutputStream("$cachePath/image.png")
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                val newFile = File(File(activity.cacheDir, "images"), "image.png")
                val uri = FileProvider.getUriForFile(
                    activity,
                    "com.rutershok.phrases.fileprovider",
                    newFile
                )
                if (uri != null) {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        .setDataAndType(uri, activity.contentResolver.getType(uri))
                        .putExtra(Intent.EXTRA_STREAM, uri)
                    activity.startActivity(Intent.createChooser(shareIntent, ""))
                }
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
            }
        }.start()
    }

    @JvmStatic
    fun saveImage(activity: Activity) {
        if (EasyPermissions.hasPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val directory = File(Key.PARENT_PATH, activity.getString(R.string.app_name))
            // Create a storage directory if it does not exist
            if (!directory.exists()) {
                directory.mkdirs()
            }
            // Create a media file name
            val selectedOutputPath =
                directory.path + File.separator + "quote_" + System.currentTimeMillis() + ".png"
            val file = File(selectedOutputPath)
            try {
                val bitmap = getBitmap(activity.findViewById(R.id.cl_background))
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                    Log.e("Bitmap", "Recycled")
                }
                Ad.showInterstitial(activity)
                //Scan files
                activity.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(
                            File(directory.absolutePath)
                        )
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Snackbar.showText(activity, R.string.image_saved)
            Ad.showInterstitial(activity)
        } else {
            EasyPermissions.requestPermissions(
                activity,
                activity.getString(R.string.rationale_ask),
                11,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    @JvmStatic
    fun privacyPolicy(context: Context) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.rutershok.netsons.org/privacy.html")
            )
        )
    }
}