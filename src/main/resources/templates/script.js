// Données des propriétés
const properties = [
    { name: 'Villa Luxueuse', type: 'villa', price: 1200000, surface: 350, rooms: 5, location: 'Côte d\'Azur', image: 'https://via.placeholder.com/400x250' },
    { name: 'Appartement Moderne', type: 'appartement', price: 300000, surface: 80, rooms: 2, location: 'Centre-ville', image: 'https://via.placeholder.com/400x250' },
    { name: 'Maison Familiale', type: 'maison', price: 500000, surface: 150, rooms: 4, location: 'Banlieue', image: 'https://via.placeholder.com/400x250' },
];

let currentPage = 1;
const propertiesPerPage = 6;

// Mise à jour du label de budget
function updatePriceLabel() {
    document.getElementById('priceLabel').innerText = `Jusqu'à ${document.getElementById('price').value}€`;
}

// Mise à jour du label de surface
function updateSurfaceLabel() {
    document.getElementById('surfaceLabel').innerText = `Jusqu'à ${document.getElementById('surface').value}m²`;
}

// Trier les propriétés
function sortProperties() {
    const sortBy = document.getElementById('sortBy').value;
    let sortedProperties = [...properties];

    if (sortBy === 'priceAsc') sortedProperties.sort((a, b) => a.price - b.price);
    else if (sortBy === 'priceDesc') sortedProperties.sort((a, b) => b.price - a.price);
    else if (sortBy === 'surfaceAsc') sortedProperties.sort((a, b) => a.surface - b.surface);
    else if (sortBy === 'surfaceDesc') sortedProperties.sort((a, b) => b.surface - a.surface);

    displayProperties(sortedProperties);
}

// Afficher les propriétés
function displayProperties(propertiesToDisplay = properties) {
    const start = (currentPage - 1) * propertiesPerPage;
    const end = start + propertiesPerPage;
    const currentProperties = propertiesToDisplay.slice(start, end);

    const propertyList = document.getElementById('propertyList');
    propertyList.innerHTML = '';

    currentProperties.forEach(property => {
        const propertyCard = `
            <div class="col-md-4 col-sm-6 mb-4">
                <div class="card property-card" onclick="openPropertyModal('${property.name}', '${property.location}', ${property.price}, ${property.surface}, ${property.rooms}, '${property.image}')">
                    <img src="${property.image}" class="card-img-top" alt="${property.name}">
                    <div class="card-body">
                        <h5 class="card-title">${property.name}</h5>
                        <p>${property.location}</p>
                        <p><strong>${property.price}€</strong></p>
                        <p>${property.surface} m² - ${property.rooms} chambres</p>
                    </div>
                </div>
            </div>
        `;
        propertyList.insertAdjacentHTML('beforeend', propertyCard);
    });
}

// Modal Détails Propriété
function openPropertyModal(name, location, price, surface, rooms, image) {
    const modalContent = document.getElementById('modalContent');
    modalContent.innerHTML = `
        <img src="${image}" class="img-fluid rounded mb-3" alt="${name}">
        <h4>${name}</h4>
        <p><strong>Localisation :</strong> ${location}</p>
        <p><strong>Prix :</strong> ${price}€</p>
        <p><strong>Surface :</strong> ${surface} m²</p>
        <p><strong>Chambres :</strong> ${rooms}</p>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus eget erat pretium euismod.</p>
    `;
    const modal = new bootstrap.Modal(document.getElementById('propertyModal'));
    modal.show();
}

// Afficher les filtres actifs
function displayActiveFilters() {
    const filters = [];
    const type = document.getElementById('type').value;
    const price = document.getElementById('price').value;
    const surface = document.getElementById('surface').value;

    if (type !== 'all') filters.push(`Type : ${type}`);
    filters.push(`Budget : ${price}€`);
    filters.push(`Surface : ${surface}m²`);

    const activeFiltersDiv = document.getElementById('activeFilters');
    activeFiltersDiv.innerHTML = filters.map(filter => `<span class="badge bg-success me-2">${filter}</span>`).join('');
}

// Filtrer les propriétés
document.getElementById('filterForm').addEventListener('submit', (e) => {
    e.preventDefault();
    displayActiveFilters();

    const type = document.getElementById('type').value;
    const price = document.getElementById('price').value;
    const surface = document.getElementById('surface').value;

    const filteredProperties = properties.filter(property => {
        return (type === 'all' || property.type === type) &&
               property.price <= price &&
               property.surface <= surface;
    });

    displayProperties(filteredProperties);
});

// Barre de recherche dynamique
document.getElementById('searchBar').addEventListener('input', (e) => {
    const keyword = e.target.value.toLowerCase();
    const filteredProperties = properties.filter(property =>
        property.name.toLowerCase().includes(keyword) ||
        property.location.toLowerCase().includes(keyword)
    );
    displayProperties(filteredProperties);
});

// Pagination
function changePage(pageNumber) {
    currentPage = pageNumber;
    displayProperties();
}

// Événements pour les sliders
document.getElementById('price').addEventListener('input', updatePriceLabel);
document.getElementById('surface').addEventListener('input', updateSurfaceLabel);

// Initialisation
updatePriceLabel();
updateSurfaceLabel();
displayProperties();
