package xyz.sky731.programming.lab8

import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Locale
import java.util.PropertyResourceBundle
import java.util.ResourceBundle

class UTF8Control : ResourceBundle.Control() {
  @Throws(IOException::class)
  override fun newBundle(baseName: String, locale: Locale, format: String, loader: ClassLoader, reload: Boolean): ResourceBundle? {
    val bundleName = toBundleName(baseName, locale)
    val resourceName = toResourceName(bundleName, "properties")
    var bundle: ResourceBundle? = null
    var stream: InputStream? = null
    if (reload) {
      val url = loader.getResource(resourceName)
      if (url != null) {
        val connection = url.openConnection()
        if (connection != null) {
          connection.useCaches = false
          stream = connection.getInputStream()
        }
      }
    } else {
      stream = loader.getResourceAsStream(resourceName)
    }
    if (stream != null) {
      try {
        bundle = PropertyResourceBundle(InputStreamReader(stream, "utf-8"))
      } finally {
        stream.close()
      }
    }
    return bundle
  }
}