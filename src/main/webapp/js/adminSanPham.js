function checkMaSanPham(maSanPhamInput, maSanPhamError, contextPath) {
    const maSanPham = maSanPhamInput.value;
    const isReadOnly = maSanPhamInput.hasAttribute('readonly');
    if (maSanPham && !isReadOnly) {
        fetch(`${contextPath}/admin-san-pham?action=checkMaSanPham&maSanPham=${maSanPham}`)
            .then(response => response.text())
            .then(data => {
                if (data === 'exists') {
                    maSanPhamInput.classList.add('is-invalid');
                    maSanPhamError.textContent = 'Đã có mã sản phẩm này.';
                } else {
                    maSanPhamInput.classList.remove('is-invalid');
                    maSanPhamError.textContent = '';
                }
            })
            .catch(error => console.error('Lỗi kiểm tra mã sản phẩm:', error));
    } else {
        maSanPhamInput.classList.remove('is-invalid');
        maSanPhamError.textContent = '';
    }
}

function initializeFormToggle(addProductButton, productForm, cancelFormButton) {
    console.log('Initializing form toggle:', { addProductButton, productForm, cancelFormButton });

    if (!addProductButton || !productForm) {
        console.error('Missing required elements:', { addProductButton, productForm });
        return;
    }

    const collapse = new bootstrap.Collapse(productForm, { toggle: false });

    function toggleAddProductButton() {
        const isFormVisible = productForm.classList.contains('show') || 
                             productForm.querySelector('input[name="action"][value="update"]') !== null;
        requestAnimationFrame(() => {
            const isCurrentlyHidden = addProductButton.classList.contains('hidden');
            if (isFormVisible !== isCurrentlyHidden) {
                addProductButton.classList.toggle('hidden', isFormVisible);
                console.log('Toggling button visibility:', { 
                    isFormVisible, 
                    hasHiddenClass: addProductButton.classList.contains('hidden'), 
                    timestamp: performance.now() 
                });
            }
        });
    }

    addProductButton.addEventListener('click', function() {
        const startTime = performance.now();
        console.log('Add product button clicked', { timestamp: startTime });

        const form = productForm.querySelector('form');
        if (!form) {
            console.error('Form not found inside productForm');
            return;
        }
        const chiTietInput = form.querySelector('#chiTiet');
        const chiTietValue = chiTietInput ? chiTietInput.value : '';
        form.reset();
        form.querySelector('input[name="action"]').value = 'add';
        const maSanPhamInput = form.querySelector('input[name="maSanPham"]');
        if (maSanPhamInput) maSanPhamInput.removeAttribute('readonly');
        const giaKhuyenMaiInput = form.querySelector('input[name="giaKhuyenMai"]');
        if (giaKhuyenMaiInput) giaKhuyenMaiInput.remove();
        if (chiTietInput) chiTietInput.value = chiTietValue;

        collapse.show();
        requestAnimationFrame(() => {
            addProductButton.classList.add('hidden');
            console.log('Button hidden and form shown', { timestamp: performance.now() });
        });
    });

    if (cancelFormButton) {
        cancelFormButton.addEventListener('click', function() {
            console.log('Cancel button clicked');
            collapse.hide();
            toggleAddProductButton();
        });
    }

    productForm.addEventListener('shown.bs.collapse', function() {
        console.log('Form shown');
        toggleAddProductButton();
    });

    productForm.addEventListener('hidden.bs.collapse', function() {
        console.log('Form hidden');
        toggleAddProductButton();
    });

    document.querySelectorAll('a[href*="action=edit"]').forEach(editLink => {
        editLink.addEventListener('click', function(e) {
            e.preventDefault();
            console.log('Edit link clicked:', this.href);

            collapse.show();
            requestAnimationFrame(() => {
                addProductButton.classList.add('hidden');
                console.log('Button hidden for edit', { timestamp: performance.now() });
                window.location.href = this.href;
            });
        });
    });

    toggleAddProductButton();
}

function initializeFormValidation(productForm, contextPath) {
    if (!productForm) return;

    const formElement = productForm.querySelector('form');
    if (!formElement) {
        console.error('Form element not found inside productForm');
        return;
    }

    formElement.addEventListener('submit', function(event) {
        const actionInput = formElement.querySelector('input[name="action"]');
        if (actionInput.value !== 'update') return;

        event.preventDefault();
        console.log('Validating update form submission');

        const giaGocInput = formElement.querySelector('input[name="giaGoc"]');
        const giaGoc = parseFloat(giaGocInput.value);
        const originalGiaGoc = parseFloat(giaGocInput.dataset.originalGiaGoc || giaGoc);

        if (isNaN(giaGoc)) {
            console.log('Invalid giaGoc:', giaGoc);
            alert('Giá gốc không hợp lệ.');
            return;
        }

        const giaKhuyenMaiHidden = formElement.querySelector('input[name="giaKhuyenMai"]');
        const giaKhuyenMai = parseFloat(giaKhuyenMaiHidden ? giaKhuyenMaiHidden.value : giaGoc);

        if (giaGoc !== originalGiaGoc) {
            if (confirm('Thay đổi giá gốc sẽ làm mất khuyến mãi? Bạn có chắc muốn thay đổi giá gốc?')) {
                if (!giaKhuyenMaiHidden) {
                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    hiddenInput.name = 'giaKhuyenMai';
                    hiddenInput.value = giaGoc;
                    formElement.appendChild(hiddenInput);
                } else {
                    giaKhuyenMaiHidden.value = giaGoc;
                }
                formElement.submit();
            }
        } else {
            if (!giaKhuyenMaiHidden) {
                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = 'giaKhuyenMai';
                hiddenInput.value = giaGoc;
                formElement.appendChild(hiddenInput);
            } else {
                giaKhuyenMaiHidden.value = giaKhuyenMai;
            }
            formElement.submit();
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const maSanPhamInput = document.getElementById('maSanPham');
    const maSanPhamError = document.getElementById('maSanPhamError');
    const addProductButton = document.getElementById('addProductButton');
    const productForm = document.getElementById('productForm');
    const cancelFormButton = document.getElementById('cancelFormButton');

    const contextPath = window.contextPath || '';

    console.log('DOM loaded, initializing:', { maSanPhamInput, maSanPhamError, addProductButton, productForm, cancelFormButton });

    if (maSanPhamInput && maSanPhamError) {
        maSanPhamInput.addEventListener('input', () => checkMaSanPham(maSanPhamInput, maSanPhamError, contextPath));
    }

    if (addProductButton && productForm) {
        initializeFormToggle(addProductButton, productForm, cancelFormButton);
    } else {
        console.error('Thiếu các thành phần cần thiết để điều khiển form', {
            addProductButton: !!addProductButton,
            productForm: !!productForm,
            cancelFormButton: !!cancelFormButton
        });
    }

    if (productForm) {
        const giaGocInput = productForm.querySelector('input[name="giaGoc"]');
        if (giaGocInput) {
            giaGocInput.dataset.originalGiaGoc = giaGocInput.value;
        }
    }

    initializeFormValidation(productForm, contextPath);
});

document.addEventListener('DOMContentLoaded', function() {
	const sidebar = document.querySelector('.sidebar');
	const toggleButton = document.createElement('button');
    toggleButton.className = 'btn btn-primary d-lg-none mb-3';
    toggleButton.innerHTML = '<i class="fas fa-bars"></i> Menu';
    toggleButton.onclick = function() {
    sidebar.classList.toggle('show');
    sidebar.classList.toggle('sidebar-hidden');
    };
    document.querySelector('.container').prepend(toggleButton);

    // Kiểm tra tải CSS
	const cssFiles = [
		`${window.contextPath}/css/adminStyles.css`,
		`${window.contextPath}/css/adminSanPham.css`,
		'https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'
	];

     cssFiles.forEach(url => {
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = url;
        link.onload = () => console.log(`Loaded: ${url}`);
        link.onerror = () => console.error(`Failed to load: ${url}`);
		document.head.appendChild(link);
      });
});