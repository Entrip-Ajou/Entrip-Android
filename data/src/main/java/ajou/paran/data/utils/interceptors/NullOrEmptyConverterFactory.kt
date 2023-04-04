package ajou.paran.data.utils.interceptors

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class NullOrEmptyConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Any?> = object : Converter<ResponseBody, Any?> {
        val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(
            this@NullOrEmptyConverterFactory,
            type,
            annotations
        )

        override fun convert(
            value: ResponseBody
        ) = when (value.contentLength() != 0L) {
            true -> nextResponseBodyConverter.convert(value)
            false -> null
        }
    }
}