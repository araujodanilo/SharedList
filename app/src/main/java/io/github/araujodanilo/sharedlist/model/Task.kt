package io.github.araujodanilo.sharedlist.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int? = -1,
    @NotNull val title: String = "",
    @NotNull var description: String = "",
    @NotNull var expectedDate: String = "",
    @NotNull val createdDate: String = "",
    @NotNull val user: String = "",
    @NotNull var finish: Boolean = false,
    @NotNull var userFinish: String = ""

): Parcelable
