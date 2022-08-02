package hu.unideb.inf.ordertracker_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.unideb.inf.ordertracker_android.databinding.ListItemOrderBinding
import hu.unideb.inf.ordertracker_android.databinding.ListItemOrderHeaderBinding
import hu.unideb.inf.ordertracker_android.model.api.Order
import hu.unideb.inf.ordertracker_android.network.OrderStatus
import hu.unideb.inf.ordertracker_android.util.DateFormat
import hu.unideb.inf.ordertracker_android.util.toFormattedString

class OrderAdapter(
    val context: Context,
    val clickListener: OrderAdapterListener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ORDER = 1

    private var adapterItems: List<DataItem>? = null

    fun setItems(orders: List<Order>) {
        val items = arrayListOf<DataItem>()

        val delivering = orders.filter { it.status == OrderStatus.DELIVERING || it.status == OrderStatus.PREPARING }
        val making = orders.filter { it.status == OrderStatus.WORKING || it.status == OrderStatus.CREATED }
        val completed = orders.filter { it.status == OrderStatus.COMPLETED }

        if (!delivering.isNullOrEmpty()) {
            items.add(DataItem.Header("Delivering"))
            delivering.forEach { deliverItem -> items.add(DataItem.OrderItem(deliverItem)) }
        }

        if (!making.isNullOrEmpty()) {
            items.add(DataItem.Header("Making"))
            making.forEach { makingItem -> items.add(DataItem.OrderItem(makingItem)) }
        }

        if (!completed.isNullOrEmpty()) {
            items.add(DataItem.Header("Completed"))
            completed.forEach { completedItem -> items.add(DataItem.OrderItem(completedItem)) }
        }

        adapterItems = items

    }

    override fun getItemViewType(position: Int): Int {
        return when (adapterItems?.get(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.OrderItem -> ITEM_VIEW_TYPE_ORDER
            else -> -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem: DataItem = adapterItems?.get(position) ?: return

        when(holder) {
            is HeaderViewHolder -> { holder.bind(currentItem as DataItem.Header, null) }
            is ViewHolder -> { holder.bind(currentItem as DataItem.OrderItem, clickListener) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> return HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ORDER -> return ViewHolder.from(parent)
        }

        return HeaderViewHolder.from(parent)
    }

    class HeaderViewHolder private constructor(val binding: ListItemOrderHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem.Header, clickListener: OrderAdapterListener? = null) {
            binding.tvTitle.text = item.title
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ListItemOrderHeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem.OrderItem, clickListener: OrderAdapterListener) {
            binding.order = item.order
            binding.clickListener = clickListener

            binding.tvCreatedAt.text =
                item.order.createdAt?.toFormattedString(DateFormat.USMMddYYYY) ?: "N/A"
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemOrderBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return adapterItems?.size ?: 0
    }
}

sealed class DataItem {
    data class OrderItem(val order: Order): DataItem()

    data class Header(val title: String): DataItem()
}

interface OrderAdapterListener {
    fun onClick(order: Order)
}