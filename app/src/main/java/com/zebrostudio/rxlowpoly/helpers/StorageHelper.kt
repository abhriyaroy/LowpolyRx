package com.zebrostudio.rxlowpoly.helpers

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.FragmentManager
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import com.zebrostudio.rxlowpoly.internal.exceptions.InvalidFileException
import io.reactivex.Single
import java.io.File

interface StorageHelper {
  fun getFileToSaveImage(fileName: String): Single<File>
  fun getUriToSaveImage(fileName: String): Single<Uri>
  fun getInputImageFileSingle(
    context: Context,
    supportFragmentManager: FragmentManager
  ): Single<File>

  fun getInputImageUriSingle(supportFragmentManager: FragmentManager): Single<Uri>
}

class StorageHelperImpl : StorageHelper {
  override fun getFileToSaveImage(fileName: String): Single<File> {
    return Single.create {
      it.onSuccess(getFile(fileName))
    }
  }

  override fun getUriToSaveImage(fileName: String): Single<Uri> {
    return Single.create {
      it.onSuccess(Uri.fromFile(getFile(fileName)))
    }
  }

  override fun getInputImageFileSingle(
    context: Context,
    supportFragmentManager: FragmentManager
  ): Single<File> {
    return Single.fromObservable(RxImagePicker.with(supportFragmentManager).requestImage(Sources.GALLERY))
      .flatMap {
        Single.just(File(getPath(context, it)))
      }
  }

  override fun getInputImageUriSingle(supportFragmentManager: FragmentManager): Single<Uri> {
    return Single.fromObservable(RxImagePicker.with(supportFragmentManager).requestImage(Sources.GALLERY))
  }

  private fun getFile(fileName: String): File {
    val directory =
      File(Environment.getExternalStorageDirectory().path + File.separator + "RxLowpolyExample")
    if (!directory.exists()) {
      directory.mkdirs()
    }
    val file = File(directory, fileName + "-" + System.currentTimeMillis() + ".png")
    if (!file.exists()) {
      file.createNewFile()
    }
    return file
  }

  @Throws(NullPointerException::class)
  private fun getPath(context: Context, uri: Uri): String? {
    val file = File(uri.path)
    if (file.exists()) {
      return uri.path
    }
    var cursor: Cursor? = null
    try {
      val projection = arrayOf(MediaStore.Images.Media.DATA)
      cursor = context.contentResolver
        .query(uri, projection, null, null, null) ?: return null
      val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
      cursor.moveToFirst()
      val s = cursor.getString(columnIndex)

      return s
    } catch (npe: NullPointerException) {
      throw InvalidFileException("Uri is not writable")
    } finally {
      cursor?.close()
    }
  }
}