package io.github.araujodanilo.sharedlist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Task (
    @PrimaryKey val title: String,
): Parcelable