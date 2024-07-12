package utils

object BytesConverter {
    fun Long.bytesToMegaBytes() = this.bytesToKyloBytes() / 1024F
    fun Long.bytesToKyloBytes() = this / 1024F
}