package com.example.kloth.data

import com.example.kloth.R

/**
 * Catálogo centralizado de datos mock. Contiene tanto las publicaciones del Feed
 * como el catálogo completo de productos para el detalle y exploración.
 */
object FakeArticle {

    // Artículos detallados (utilizados en Detail y Explore)
    val abrigoNegro = ProductDetailData(
        id = "abrigo_negro",
        title = "Abrigo Lunar Negro",
        brand = "Maison de l'Éther",
        price = "2.450€",
        categoryTag = "Prenda",
        colorName = "Nebulosity Dark",
        categoryName = "Outerwear Elite",
        description = "Una pieza maestra de sastrería contemporánea diseñada para ofrecer máxima elegancia y confort térmico.",
        imageRes = R.drawable.abrigo_negro,
        averageRating = 4.9f,
        reviewsCountText = "(128 reseñas)"
    )

    val camisetaGymshark = ProductDetailData(
        id = "camiseta_gymshark",
        title = "Camiseta GymShark",
        brand = "Gymshark",
        price = "45€",
        categoryTag = "Prenda",
        colorName = "Negro Carbón",
        categoryName = "Activewear",
        description = "Diseño técnico de compresión ligera con tejido transpirable de secado rápido, ideal para entrenamientos de alta intensidad.",
        imageRes = R.drawable.camiseta_gymshark,
        averageRating = 4.8f,
        reviewsCountText = "(84 reseñas)"
    )

    val camisetaBarcelona = ProductDetailData(
        id = "camiseta_barcelona",
        title = "Jersey Barcelona",
        brand = "FC Barcelona",
        price = "99€",
        categoryTag = "Prenda",
        colorName = "Azul Grana",
        categoryName = "Fútbol",
        description = "Primera equipación oficial con tecnología de ventilación estratégica y escudo bordado. Edición de temporada.",
        imageRes = R.drawable.camiseta_barcelona,
        averageRating = 5.0f,
        reviewsCountText = "(42 reseñas)"
    )

    val bolsoCuero = ProductDetailData(
        id = "bolso_cuero",
        title = "Bolso Cuero",
        brand = "Cuero & Co.",
        price = "320€",
        categoryTag = "Prenda",
        colorName = "Marrón Tabaco",
        categoryName = "Accesorios",
        description = "Bolso artesanal en piel plena vegetal con herrajes en latón envejecido. Envejece con carácter y resistencia excepcional.",
        imageRes = R.drawable.bolso_cuero,
        averageRating = 4.7f,
        reviewsCountText = "(215 reseñas)"
    )

    // Detalles vinculados a los posts del Feed con sus URLs originales
    val detailSuit = ProductDetailData(
        id = "post_suit_1",
        title = "Suit elegante negro",
        brand = "Atelier Noir",
        price = "890€",
        categoryTag = "Outfit",
        colorName = "Noche Intensa",
        categoryName = "Sastrería",
        description = "Este traje define la elegancia y minimalismo con sus cortes precisos. Ideal para eventos formales donde la sobriedad es la clave.",
        imageUrl = "https://i.pinimg.com/564x/0b/71/5d/0b715d6e24e6a80caf83ebaebac322ce.jpg",
        averageRating = 4.8f,
        reviewsCountText = "(124 reseñas)"
    )

    val detailVestido = ProductDetailData(
        id = "post_vestido_2",
        title = "Vestido blanco largo",
        brand = "Casa Lumière",
        price = "620€",
        categoryTag = "Prenda",
        colorName = "Blanco Perla",
        categoryName = "Vestidos",
        description = "Este vestido es la muestra de que no se necesita algo extravagante para brillar. Tela ligera y fresca para el verano.",
        imageUrl = "https://m.media-amazon.com/images/I/41VWMm4OlpL.jpg",
        averageRating = 4.5f,
        reviewsCountText = "(89 reseñas)"
    )

    val detailCozy = ProductDetailData(
        id = "post_cozy_3",
        title = "Outfit cotidiano cozy",
        brand = "Retro Denim Co.",
        price = "240€",
        categoryTag = "Outfit",
        colorName = "Azul Desgastado",
        categoryName = "Vintage",
        description = "Una pieza única recuperada de los años 90. El desgaste natural le da un carácter que ninguna prenda nueva puede imitar.",
        imageUrl = "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?q=80&w=600&auto=format&fit=crop",
        averageRating = 4.9f,
        reviewsCountText = "(210 reseñas)"
    )

    val detailAccesorios = ProductDetailData(
        id = "post_acc_4",
        title = "Accesorios dorados",
        brand = "Aurum Atelier",
        price = "1.150€",
        categoryTag = "Prenda",
        colorName = "Oro 18k",
        categoryName = "Joyería",
        description = "Complementos que elevan cualquier outfit básico. Joyería artesanal con acabados en oro de 18k.",
        imageUrl = "https://images.unsplash.com/photo-1537832816519-689ad163238b?q=80&w=600&auto=format&fit=crop",
        averageRating = 4.7f,
        reviewsCountText = "(56 reseñas)"
    )

    val detailStreetwear = ProductDetailData(
        id = "post_street_5",
        title = "Streetwear urbano",
        brand = "Urban Threads",
        price = "130€",
        categoryTag = "Prenda",
        colorName = "Gris Asfalto",
        categoryName = "Streetwear",
        description = "Sudadera oversize combinada con joggers técnicos. La comodidad no está reñida con el estilo en la ciudad.",
        imageUrl = "https://images.unsplash.com/photo-1512436991641-6745cdb1723f?q=80&w=600&auto=format&fit=crop",
        averageRating = 4.6f,
        reviewsCountText = "(145 reseñas)"
    )

    val detailPrimavera = ProductDetailData(
        id = "post_primavera_6",
        title = "Colección Primavera",
        brand = "Fleur & Co.",
        price = "380€",
        categoryTag = "Prenda",
        colorName = "Multicolor Floral",
        categoryName = "Colecciones",
        description = "Estampados florales y colores vibrantes para recibir la nueva estación. Tejidos transpirables y cortes fluidos.",
        imageUrl = "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?q=80&w=600&auto=format&fit=crop",
        averageRating = 4.4f,
        reviewsCountText = "(78 reseñas)"
    )

    /** Catálogo completo unificado. Se utiliza para la pantalla Explore. */
    val allProducts = listOf(
        abrigoNegro,
        camisetaGymshark,
        camisetaBarcelona,
        bolsoCuero,
        detailSuit,
        detailVestido,
        detailCozy,
        detailAccesorios,
        detailStreetwear,
        detailPrimavera
    )

    /** Lista de publicaciones para el Feed. */
    val posts = listOf(
        PostItem(
            id = "1",
            creatorName = "Isabela",
            timeAgo = "2 horas",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=200&h=200&auto=format&fit=crop",
            commentsCount = "42",
            tags = listOf("minimalista", "otoño", "black outfits"),
            product = detailSuit
        ),
        PostItem(
            id = "2",
            creatorName = "Mary Luz",
            timeAgo = "5 horas",
            avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=200&h=200&auto=format&fit=crop",
            commentsCount = "18",
            tags = listOf("minimalista", "verano", "timeless"),
            product = detailVestido
        ),
        PostItem(
            id = "3",
            creatorName = "Ana",
            timeAgo = "1 día",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=200&h=200&auto=format&fit=crop",
            commentsCount = "65",
            tags = listOf("vintage", "denim", "90s"),
            product = detailCozy
        ),
        PostItem(
            id = "4",
            creatorName = "Elena Vancouvé",
            timeAgo = "3 horas",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=200&h=200&auto=format&fit=crop",
            commentsCount = "12",
            tags = listOf("joyería", "luxury", "detalles"),
            product = detailAccesorios
        ),
        PostItem(
            id = "5",
            creatorName = "Carlos Ruiz",
            timeAgo = "6 horas",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=200&h=200&auto=format&fit=crop",
            commentsCount = "30",
            tags = listOf("streetwear", "comodidad", "urban"),
            product = detailStreetwear
        ),
        PostItem(
            id = "6",
            creatorName = "Sofia",
            timeAgo = "8 horas",
            avatarUrl = "https://images.unsplash.com/photo-1488423191181-016ce2632205?q=80&w=200&h=200&auto=format&fit=crop",
            commentsCount = "22",
            tags = listOf("primavera", "floral", "boho"),
            product = detailPrimavera
        )
    )

    /**
     * Resuelve el artículo correspondiente a un identificador.
     * Devuelve null si el id no existe o no es válido.
     */
    fun obtenerProductoPorId(productId: String): ProductDetailData? =
        allProducts.firstOrNull { it.id == productId }
}
