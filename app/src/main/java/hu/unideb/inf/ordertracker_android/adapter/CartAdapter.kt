package hu.unideb.inf.ordertracker_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.databinding.ListItemCartBinding
import hu.unideb.inf.ordertracker_android.model.api.Product
import hu.unideb.inf.ordertracker_android.util.FileUtils

class CartAdapter(
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    val clickListener: CartAdapterListener
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var products: List<Product>? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        products?.get(position)?.let {
            holder.bind(it, position, clickListener, context, lifecycleOwner)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemCartBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product, position: Int, clickListener: CartAdapterListener, context: Context, lifecycleOwner: LifecycleOwner) {
            binding.product = item
            binding.clickListener = clickListener
            binding.position = position

            binding.lifecycleOwner = lifecycleOwner

            val imageFile =
                FileUtils.findPath(context, FileUtils.Directory.IMAGES, "${item.id}_thumb.jpg")
            Glide.with(context)
                .load(imageFile)
                .placeholder(
                    ContextCompat.getDrawable(context, R.drawable.ic_burger)
                        ?.also { it.setTint(ContextCompat.getColor(context, R.color.black)) })
                .into(binding.ivMainImage)

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCartBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return products?.size ?: 0
    }

}

interface CartAdapterListener {
    fun onClick(product: Product)
    fun onIncreaseClick(product: Product, position: Int)
    fun onDecreaseClick(product: Product, position: Int)
    fun onDeleteClick(product: Product, position: Int)
}