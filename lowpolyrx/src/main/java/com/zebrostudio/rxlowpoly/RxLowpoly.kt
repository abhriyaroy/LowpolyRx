package com.zebrostudio.rxlowpoly

import android.app.Application
import android.content.Context
import com.zebrostudio.rxlowpoly.internal.builder.RxLowpolyBuilder
import com.zebrostudio.rxlowpoly.internal.exceptions.WrongContextException

object RxLowpoly {

  /**
   * Entry point of the RxLowpolyBuilder.
   * Application context is required to run outside of activity scope.
   *
   *  @param applicationContext context.
   *  @return [RxLowpolyBuilder].
   */
  @JvmStatic
  @Throws(WrongContextException::class)
  fun with(applicationContext: Context): RxLowpolyBuilder {
    if (applicationContext is Application) {
      return RxLowpolyBuilder().init(applicationContext)
    } else {
      throw WrongContextException()
    }
  }

}