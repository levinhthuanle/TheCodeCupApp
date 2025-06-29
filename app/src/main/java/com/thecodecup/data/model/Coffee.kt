import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coffee(
    val id: Int,
    val name: String,
    val price: Double,
    val imageResId: Int
) : Parcelable
