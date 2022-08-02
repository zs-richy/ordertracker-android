package hu.unideb.inf.ordertracker_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.databinding.ListItemProductBinding
import hu.unideb.inf.ordertracker_android.model.api.Product
import hu.unideb.inf.ordertracker_android.util.FileUtils

class ProductAdapter(
    val context: Context,
    val clickListener: ProductAdapterListener
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var products: List<Product>? = null

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        products?.get(position)?.let {
            holder.bind(it, clickListener, context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product, clickListener: ProductAdapterListener, context: Context) {
            binding.product = item
            binding.clickListener = clickListener

            val imageFile =
                FileUtils.findPath(context, FileUtils.Directory.IMAGES, "${item.id}_thumb.jpg")
            Glide.with(context)
                .load(imageFile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(
                    ContextCompat.getDrawable(context, R.drawable.ic_burger)
                        ?.also { it.setTint(ContextCompat.getColor(context, R.color.black)) })
                .into(binding.ivMainImage)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemProductBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return products?.size ?: 0
    }
}

interface ProductAdapterListener {
    fun onClick(product: Product)
}