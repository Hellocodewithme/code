// Product Data
const products = [
  { id: 1, name: "Wireless Headphones", category: "Electronics", price: 79.99, image: "🎧", rating: 4.5, reviews: 128, stock: 45, description: "Premium noise-canceling wireless headphones with 30-hour battery life", colors: ["Black", "Silver", "Blue"] },
  { id: 2, name: "Smartphone Stand", category: "Electronics", price: 19.99, image: "📱", rating: 4.2, reviews: 89, stock: 120, description: "Adjustable aluminum smartphone stand for desk" },
  { id: 3, name: "Organic Cotton T-Shirt", category: "Fashion", price: 34.99, image: "👕", rating: 4.7, reviews: 234, stock: 200, description: "Comfortable organic cotton t-shirt available in multiple colors", sizes: ["XS", "S", "M", "L", "XL", "XXL"], colors: ["White", "Black", "Gray", "Navy"] },
  { id: 4, name: "Running Shoes", category: "Sports", price: 129.99, image: "👟", rating: 4.6, reviews: 567, stock: 85, description: "Professional running shoes with advanced cushioning technology", sizes: ["6", "7", "8", "9", "10", "11", "12", "13"], colors: ["Black", "Red", "Blue", "White"] },
  { id: 5, name: "Yoga Mat", category: "Sports", price: 44.99, image: "🧘", rating: 4.4, reviews: 156, stock: 110, description: "Non-slip eco-friendly yoga mat with carrying strap" },
  { id: 6, name: "Desk Lamp", category: "Home & Garden", price: 54.99, image: "💡", rating: 4.3, reviews: 78, stock: 65, description: "LED desk lamp with adjustable brightness and color temperature" },
  { id: 7, name: "Coffee Maker", category: "Home & Garden", price: 89.99, image: "☕", rating: 4.8, reviews: 342, stock: 40, description: "Programmable coffee maker with thermal carafe" },
  { id: 8, name: "Portable Charger", category: "Electronics", price: 39.99, image: "🔋", rating: 4.5, reviews: 201, stock: 88, description: "20000mAh fast charging portable power bank", colors: ["Black", "White", "Silver"] },
  { id: 9, name: "Winter Jacket", category: "Fashion", price: 189.99, image: "🧥", rating: 4.6, reviews: 289, stock: 55, description: "Waterproof insulated winter jacket with hood", sizes: ["XS", "S", "M", "L", "XL", "XXL"], colors: ["Black", "Navy", "Gray"] },
  { id: 10, name: "Dumbbells Set", category: "Sports", price: 149.99, image: "🏋️", rating: 4.7, reviews: 123, stock: 30, description: "Adjustable dumbbells set 5-25lbs with stand" },
  { id: 11, name: "Bedding Set", category: "Home & Garden", price: 79.99, image: "🛏️", rating: 4.4, reviews: 156, stock: 72, description: "Luxury 1000 thread count cotton bedding set" },
  { id: 12, name: "Wireless Mouse", category: "Electronics", price: 29.99, image: "🖱️", rating: 4.3, reviews: 245, stock: 200, description: "Silent ergonomic wireless mouse with 18-month battery", colors: ["Black", "Gray", "White"] },
  { id: 13, name: "Jeans", category: "Fashion", price: 69.99, image: "👖", rating: 4.5, reviews: 412, stock: 180, description: "Classic slim-fit denim jeans", sizes: ["28", "30", "32", "34", "36", "38", "40"], colors: ["Dark Blue", "Light Blue", "Black"] },
  { id: 14, name: "Bicycle Helmet", category: "Sports", price: 84.99, image: "🪖", rating: 4.8, reviews: 89, stock: 45, description: "Lightweight safety-certified bicycle helmet", sizes: ["S", "M", "L"] },
  { id: 15, name: "Plant Pot", category: "Home & Garden", price: 24.99, image: "🪴", rating: 4.2, reviews: 67, stock: 95, description: "Ceramic plant pot with drainage hole" },
  { id: 16, name: "USB-C Cable", category: "Electronics", price: 12.99, image: "🔌", rating: 4.4, reviews: 523, stock: 500, description: "3ft braided USB-C charging and data cable", colors: ["Black", "White"] },
  { id: 17, name: "Sneakers", category: "Fashion", price: 94.99, image: "👟", rating: 4.6, reviews: 234, stock: 140, description: "Comfortable casual sneakers with cushioned sole", sizes: ["6", "7", "8", "9", "10", "11", "12", "13"], colors: ["White", "Black", "Gray"] },
  { id: 18, name: "Resistance Bands", category: "Sports", price: 22.99, image: "💪", rating: 4.5, reviews: 178, stock: 220, description: "Set of 5 resistance bands with varying resistance levels" },
  { id: 19, name: "Table Lamp", category: "Home & Garden", price: 45.99, image: "🕯️", rating: 4.3, reviews: 92, stock: 60, description: "Modern table lamp with linen shade" },
  { id: 20, name: "Bluetooth Speaker", category: "Electronics", price: 59.99, image: "🔊", rating: 4.7, reviews: 456, stock: 95, description: "Portable waterproof bluetooth speaker with 12-hour battery", colors: ["Black", "Blue", "Red"] }
];

const TAX_RATE = 0.08;
const FREE_SHIPPING_THRESHOLD = 50;
const STANDARD_SHIPPING = 0;
const EXPRESS_SHIPPING = 10;

// State Management
let cart = [];
let currentPage = 'home';
let currentProduct = null;
let filteredProducts = [...products];
let selectedSize = null;
let selectedColor = null;
let orderData = null;

// Utility Functions
function formatPrice(price) {
  return `$${price.toFixed(2)}`;
}

function generateStars(rating) {
  const fullStars = Math.floor(rating);
  const hasHalfStar = rating % 1 >= 0.5;
  let stars = '★'.repeat(fullStars);
  if (hasHalfStar) stars += '☆';
  const emptyStars = 5 - Math.ceil(rating);
  stars += '☆'.repeat(emptyStars);
  return stars;
}

function calculateCartTotals() {
  const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  const tax = subtotal * TAX_RATE;
  const shippingMethod = document.querySelector('input[name="shipping"]:checked')?.value || 'standard';
  let shipping = 0;
  
  if (subtotal > 0) {
    if (shippingMethod === 'express') {
      shipping = EXPRESS_SHIPPING;
    } else if (subtotal < FREE_SHIPPING_THRESHOLD) {
      shipping = 5;
    }
  }
  
  const total = subtotal + tax + shipping;
  
  return { subtotal, tax, shipping, total };
}

function updateCartCount() {
  const count = cart.reduce((sum, item) => sum + item.quantity, 0);
  document.getElementById('cartCount').textContent = count;
}

// Navigation
function showPage(pageName) {
  document.querySelectorAll('.page').forEach(page => {
    page.classList.remove('active');
  });
  
  const pageElement = document.getElementById(`${pageName}Page`);
  if (pageElement) {
    pageElement.classList.add('active');
    currentPage = pageName;
  }
  
  window.scrollTo(0, 0);
}

// Product Rendering
function renderProducts(productsToRender = filteredProducts) {
  const grid = document.getElementById('productGrid');
  
  if (productsToRender.length === 0) {
    grid.innerHTML = '<div class="empty-cart"><p>No products found.</p></div>';
    return;
  }
  
  grid.innerHTML = productsToRender.map(product => `
    <div class="product-card" onclick="viewProduct(${product.id})">
      <div class="product-image">${product.image}</div>
      <div class="product-info">
        <div class="product-name">${product.name}</div>
        <div class="product-price">${formatPrice(product.price)}</div>
        <div class="product-rating">
          <span class="stars">${generateStars(product.rating)}</span>
          <span>(${product.reviews})</span>
        </div>
        <div class="product-actions">
          <button class="btn-add-cart" onclick="event.stopPropagation(); quickAddToCart(${product.id})">Add to Cart</button>
        </div>
      </div>
    </div>
  `).join('');
}

function quickAddToCart(productId) {
  const product = products.find(p => p.id === productId);
  if (!product) return;
  
  const existingItem = cart.find(item => 
    item.id === productId && 
    !item.size && 
    !item.color
  );
  
  if (existingItem) {
    existingItem.quantity++;
  } else {
    cart.push({
      id: product.id,
      name: product.name,
      price: product.price,
      image: product.image,
      quantity: 1
    });
  }
  
  updateCartCount();
  showNotification('Added to cart!');
}

function showNotification(message) {
  // Simple notification (could be enhanced with a toast component)
  alert(message);
}

// Product Detail
function viewProduct(productId) {
  const product = products.find(p => p.id === productId);
  if (!product) return;
  
  currentProduct = product;
  selectedSize = null;
  selectedColor = null;
  
  const detailPage = document.getElementById('productDetailPage');
  detailPage.innerHTML = `
    <div class="product-detail">
      <div class="product-detail-image">${product.image}</div>
      <div class="product-detail-info">
        <h2>${product.name}</h2>
        <div class="product-detail-price">${formatPrice(product.price)}</div>
        <div class="stock-status">In Stock: ${product.stock} units</div>
        <div class="product-rating">
          <span class="stars">${generateStars(product.rating)}</span>
          <span>${product.rating} (${product.reviews} reviews)</span>
        </div>
        <div class="product-description">${product.description}</div>
        
        <div class="product-options" id="productOptions">
          ${product.sizes ? `
            <div class="option-group">
              <label class="option-label">Size:</label>
              <div class="option-buttons" id="sizeOptions">
                ${product.sizes.map(size => `
                  <button class="option-btn" onclick="selectSize('${size}')">${size}</button>
                `).join('')}
              </div>
            </div>
          ` : ''}
          
          ${product.colors ? `
            <div class="option-group">
              <label class="option-label">Color:</label>
              <div class="option-buttons" id="colorOptions">
                ${product.colors.map(color => `
                  <button class="option-btn" onclick="selectColor('${color}')">${color}</button>
                `).join('')}
              </div>
            </div>
          ` : ''}
        </div>
        
        <div class="quantity-selector">
          <label class="option-label">Quantity:</label>
          <button class="quantity-btn" onclick="decrementQuantity()">−</button>
          <span class="quantity-value" id="productQuantity">1</span>
          <button class="quantity-btn" onclick="incrementQuantity()">+</button>
        </div>
        
        <div class="detail-actions">
          <button class="btn btn--primary" style="flex: 1;" onclick="addToCartFromDetail()">Add to Cart</button>
          <button class="btn-wishlist" onclick="addToWishlist()">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
            </svg>
          </button>
        </div>
        
        <div class="reviews-section">
          <h3>Customer Reviews</h3>
          ${generateSampleReviews(product)}
        </div>
      </div>
    </div>
  `;
  
  showPage('productDetail');
}

function generateSampleReviews(product) {
  const sampleReviews = [
    { author: "John D.", rating: 5, text: "Excellent product! Highly recommended." },
    { author: "Sarah M.", rating: 4, text: "Good quality, exactly as described." },
    { author: "Mike R.", rating: 5, text: "Very satisfied with my purchase!" }
  ];
  
  return sampleReviews.map(review => `
    <div class="review">
      <div class="review-header">
        <div class="review-author">${review.author}</div>
        <div class="stars">${generateStars(review.rating)}</div>
      </div>
      <div class="review-text">${review.text}</div>
    </div>
  `).join('');
}

function selectSize(size) {
  selectedSize = size;
  document.querySelectorAll('#sizeOptions .option-btn').forEach(btn => {
    btn.classList.remove('selected');
    if (btn.textContent === size) {
      btn.classList.add('selected');
    }
  });
}

function selectColor(color) {
  selectedColor = color;
  document.querySelectorAll('#colorOptions .option-btn').forEach(btn => {
    btn.classList.remove('selected');
    if (btn.textContent === color) {
      btn.classList.add('selected');
    }
  });
}

function incrementQuantity() {
  const quantityEl = document.getElementById('productQuantity');
  let quantity = parseInt(quantityEl.textContent);
  if (quantity < currentProduct.stock) {
    quantityEl.textContent = quantity + 1;
  }
}

function decrementQuantity() {
  const quantityEl = document.getElementById('productQuantity');
  let quantity = parseInt(quantityEl.textContent);
  if (quantity > 1) {
    quantityEl.textContent = quantity - 1;
  }
}

function addToCartFromDetail() {
  if (!currentProduct) return;
  
  // Check if size/color is required but not selected
  if (currentProduct.sizes && !selectedSize) {
    showNotification('Please select a size');
    return;
  }
  if (currentProduct.colors && !selectedColor) {
    showNotification('Please select a color');
    return;
  }
  
  const quantity = parseInt(document.getElementById('productQuantity').textContent);
  
  // Check if item with same options exists
  const existingItem = cart.find(item => 
    item.id === currentProduct.id && 
    item.size === selectedSize && 
    item.color === selectedColor
  );
  
  if (existingItem) {
    existingItem.quantity += quantity;
  } else {
    cart.push({
      id: currentProduct.id,
      name: currentProduct.name,
      price: currentProduct.price,
      image: currentProduct.image,
      quantity: quantity,
      size: selectedSize,
      color: selectedColor
    });
  }
  
  updateCartCount();
  showNotification('Added to cart!');
}

function addToWishlist() {
  showNotification('Added to wishlist!');
}

// Shopping Cart
function renderCart() {
  const cartItems = document.getElementById('cartItems');
  
  if (cart.length === 0) {
    cartItems.innerHTML = '<div class="empty-cart"><p>Your cart is empty</p></div>';
    document.getElementById('cartSubtotal').textContent = '$0.00';
    document.getElementById('cartTax').textContent = '$0.00';
    document.getElementById('cartShipping').textContent = '$0.00';
    document.getElementById('cartTotal').textContent = '$0.00';
    return;
  }
  
  cartItems.innerHTML = cart.map((item, index) => `
    <div class="cart-item">
      <div class="cart-item-image">${item.image}</div>
      <div class="cart-item-details">
        <div class="cart-item-name">${item.name}</div>
        <div class="cart-item-price">${formatPrice(item.price)}</div>
        ${item.size || item.color ? `
          <div class="cart-item-options">
            ${item.size ? `Size: ${item.size}` : ''}
            ${item.size && item.color ? ' | ' : ''}
            ${item.color ? `Color: ${item.color}` : ''}
          </div>
        ` : ''}
        <div class="cart-item-controls">
          <div class="cart-quantity">
            <button onclick="updateCartQuantity(${index}, -1)">−</button>
            <span>${item.quantity}</span>
            <button onclick="updateCartQuantity(${index}, 1)">+</button>
          </div>
          <button class="btn-remove" onclick="removeFromCart(${index})">Remove</button>
        </div>
      </div>
      <div class="cart-item-subtotal">${formatPrice(item.price * item.quantity)}</div>
    </div>
  `).join('');
  
  updateCartSummary();
}

function updateCartQuantity(index, change) {
  const item = cart[index];
  const newQuantity = item.quantity + change;
  
  if (newQuantity <= 0) {
    removeFromCart(index);
  } else {
    const product = products.find(p => p.id === item.id);
    if (newQuantity <= product.stock) {
      item.quantity = newQuantity;
      renderCart();
      updateCartCount();
    }
  }
}

function removeFromCart(index) {
  cart.splice(index, 1);
  renderCart();
  updateCartCount();
}

function updateCartSummary() {
  const totals = calculateCartTotals();
  
  document.getElementById('cartSubtotal').textContent = formatPrice(totals.subtotal);
  document.getElementById('cartTax').textContent = formatPrice(totals.tax);
  document.getElementById('cartShipping').textContent = totals.shipping === 0 ? 'Free' : formatPrice(totals.shipping);
  document.getElementById('cartTotal').textContent = formatPrice(totals.total);
}

// Checkout
function renderCheckout() {
  const checkoutItems = document.getElementById('checkoutItems');
  
  checkoutItems.innerHTML = cart.map(item => `
    <div class="checkout-item">
      <div class="checkout-item-image">${item.image}</div>
      <div class="checkout-item-info">
        <div class="checkout-item-name">${item.name}</div>
        <div class="checkout-item-details">
          Qty: ${item.quantity} × ${formatPrice(item.price)}
          ${item.size ? ` | Size: ${item.size}` : ''}
          ${item.color ? ` | Color: ${item.color}` : ''}
        </div>
      </div>
    </div>
  `).join('');
  
  updateCheckoutSummary();
}

function updateCheckoutSummary() {
  const totals = calculateCartTotals();
  
  document.getElementById('checkoutSubtotal').textContent = formatPrice(totals.subtotal);
  document.getElementById('checkoutTax').textContent = formatPrice(totals.tax);
  document.getElementById('checkoutShipping').textContent = totals.shipping === 0 ? 'Free' : formatPrice(totals.shipping);
  document.getElementById('checkoutTotal').textContent = formatPrice(totals.total);
}

function placeOrder() {
  // Validate form
  const name = document.getElementById('checkoutName').value.trim();
  const email = document.getElementById('checkoutEmail').value.trim();
  const address = document.getElementById('checkoutAddress').value.trim();
  const city = document.getElementById('checkoutCity').value.trim();
  const state = document.getElementById('checkoutState').value.trim();
  const zip = document.getElementById('checkoutZip').value.trim();
  const phone = document.getElementById('checkoutPhone').value.trim();
  
  if (!name || !email || !address || !city || !state || !zip || !phone) {
    showNotification('Please fill in all required fields');
    return;
  }
  
  const shippingMethod = document.querySelector('input[name="shipping"]:checked').value;
  const paymentMethod = document.querySelector('input[name="payment"]:checked').value;
  const totals = calculateCartTotals();
  
  // Generate order
  orderData = {
    orderNumber: 'ORD-' + Math.random().toString(36).substr(2, 9).toUpperCase(),
    date: new Date(),
    items: [...cart],
    totals: totals,
    shipping: shippingMethod,
    payment: paymentMethod,
    customer: { name, email, address, city, state, zip, phone }
  };
  
  // Clear cart
  cart = [];
  updateCartCount();
  
  // Show confirmation
  renderConfirmation();
  showPage('confirmation');
}

// Order Confirmation
function renderConfirmation() {
  if (!orderData) return;
  
  const deliveryDate = new Date(orderData.date);
  deliveryDate.setDate(deliveryDate.getDate() + (orderData.shipping === 'express' ? 3 : 7));
  
  document.getElementById('orderNumber').textContent = orderData.orderNumber;
  document.getElementById('estimatedDelivery').textContent = deliveryDate.toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric' 
  });
  
  const confirmationItems = document.getElementById('confirmationItems');
  confirmationItems.innerHTML = orderData.items.map(item => `
    <div class="confirmation-item">
      <div>
        <div style="font-weight: 500;">${item.name}</div>
        <div style="font-size: 12px; color: var(--color-text-secondary);">
          Qty: ${item.quantity}
          ${item.size ? ` | Size: ${item.size}` : ''}
          ${item.color ? ` | Color: ${item.color}` : ''}
        </div>
      </div>
      <div>${formatPrice(item.price * item.quantity)}</div>
    </div>
  `).join('');
  
  document.getElementById('confirmationTotal').textContent = formatPrice(orderData.totals.total);
}

// Filters and Sorting
function applyFilters() {
  const category = document.getElementById('categoryFilter').value;
  const sortBy = document.getElementById('sortSelect').value;
  const searchTerm = document.getElementById('searchInput').value.toLowerCase();
  
  // Filter by category
  filteredProducts = products.filter(product => {
    const matchesCategory = category === 'all' || product.category === category;
    const matchesSearch = searchTerm === '' || 
      product.name.toLowerCase().includes(searchTerm) ||
      product.description.toLowerCase().includes(searchTerm);
    return matchesCategory && matchesSearch;
  });
  
  // Sort
  switch(sortBy) {
    case 'price-low':
      filteredProducts.sort((a, b) => a.price - b.price);
      break;
    case 'price-high':
      filteredProducts.sort((a, b) => b.price - a.price);
      break;
    case 'rating':
      filteredProducts.sort((a, b) => b.rating - a.rating);
      break;
    case 'reviews':
      filteredProducts.sort((a, b) => b.reviews - a.reviews);
      break;
    default:
      // Default order
      filteredProducts.sort((a, b) => a.id - b.id);
  }
  
  renderProducts(filteredProducts);
}

// Event Listeners
document.addEventListener('DOMContentLoaded', () => {
  // Initial render
  renderProducts();
  updateCartCount();
  
  // Header logo - go home
  document.querySelector('.header-logo h1').addEventListener('click', () => {
    showPage('home');
    applyFilters();
  });
  
  // Search
  document.getElementById('searchBtn').addEventListener('click', applyFilters);
  document.getElementById('searchInput').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') applyFilters();
  });
  
  // Cart button
  document.getElementById('cartBtn').addEventListener('click', () => {
    renderCart();
    showPage('cart');
  });
  
  // Filters and sort
  document.getElementById('categoryFilter').addEventListener('change', applyFilters);
  document.getElementById('sortSelect').addEventListener('change', applyFilters);
  
  // Cart actions
  document.getElementById('continueShopping').addEventListener('click', () => {
    showPage('home');
  });
  
  document.getElementById('proceedCheckout').addEventListener('click', () => {
    if (cart.length === 0) {
      showNotification('Your cart is empty');
      return;
    }
    renderCheckout();
    showPage('checkout');
  });
  
  // Checkout - shipping method change
  document.querySelectorAll('input[name="shipping"]').forEach(radio => {
    radio.addEventListener('change', () => {
      updateCheckoutSummary();
    });
  });
  
  // Place order
  document.getElementById('placeOrder').addEventListener('click', placeOrder);
  
  // Continue shopping from confirmation
  document.getElementById('continueShoppingConfirm').addEventListener('click', () => {
    showPage('home');
    applyFilters();
  });
});