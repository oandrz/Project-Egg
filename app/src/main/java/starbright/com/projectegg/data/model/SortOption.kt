package starbright.com.projectegg.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SortOption(
    val id: String,
    val imageUrl: String
) : Parcelable