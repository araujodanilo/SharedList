package io.github.araujodanilo.sharedlist.adapter

interface OnTaskClickListener {
    fun onTileTaskClick(position: Int)
    fun onEditMenuItemClick(position: Int)
    fun onRemoveMenuItemClick(position: Int)
    fun onFinishTask(position: Int)
}