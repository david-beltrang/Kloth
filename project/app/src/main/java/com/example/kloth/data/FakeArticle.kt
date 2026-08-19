package com.example.kloth.data

import com.example.kloth.ui.theme.RedInferno

object FakeArticle {
    val posts = listOf(
        PostItem(
            id = "1",
            creatorName = "Isabela",
            timeAgo = "2 horas",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=200&h=200&auto=format&fit=crop",
            postImageUrl = "https://i.pinimg.com/564x/0b/71/5d/0b715d6e24e6a80caf83ebaebac322ce.jpg",
            title = "Suit elegante negro",
            rating = "4.8",
            reviewCount = 124,
            comments = "42",
            description = "Este traje define la elegancia y minimalismo con sus cortes precisos. Ideal para eventos formales donde la sobriedad es la clave.",
            tags = listOf("minimalista", "otoño", "black outfits"),
            category = "OUTFIT",
            categoryColor = RedInferno
        ),
        PostItem(
            id = "2",
            creatorName = "Mary Luz",
            timeAgo = "5 horas",
            avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=200&h=200&auto=format&fit=crop",
            postImageUrl = "https://m.media-amazon.com/images/I/41VWMm4OlpL.jpg",
            title = "Vestido blanco largo",
            rating = "4.5",
            reviewCount = 89,
            comments = "18",
            description = "Este vestido es la muestra de que no se necesita algo extravagante para brillar. Tela ligera y fresca para el verano.",
            tags = listOf("minimalista", "verano", "timeless"),
            category = "PRENDA",
            categoryColor = RedInferno
        ),
        PostItem(
            id = "3",
            creatorName = "Ana",
            timeAgo = "1 día",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=200&h=200&auto=format&fit=crop",
            postImageUrl = "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?q=80&w=600&auto=format&fit=crop",
            title = "Outfit cotidiano cozy",
            rating = "4.9",
            reviewCount = 210,
            comments = "65",
            description = "Una pieza única recuperada de los años 90. El desgaste natural le da un carácter que ninguna prenda nueva puede imitar.",
            tags = listOf("vintage", "denim", "90s"),
            category = "OUTFIT",
            categoryColor = RedInferno
        ),
        PostItem(
            id = "4",
            creatorName = "Elena Vancouvé",
            timeAgo = "3 horas",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=200&h=200&auto=format&fit=crop",
            postImageUrl = "https://images.unsplash.com/photo-1537832816519-689ad163238b?q=80&w=600&auto=format&fit=crop",
            title = "Accesorios dorados",
            rating = "4.7",
            reviewCount = 56,
            comments = "12",
            description = "Complementos que elevan cualquier outfit básico. Joyería artesanal con acabados en oro de 18k.",
            tags = listOf("joyería", "luxury", "detalles"),
            category = "PRENDA",
            categoryColor = RedInferno
        ),
        PostItem(
            id = "5",
            creatorName = "Carlos Ruiz",
            timeAgo = "6 horas",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=200&h=200&auto=format&fit=crop",
            postImageUrl = "https://images.unsplash.com/photo-1512436991641-6745cdb1723f?q=80&w=600&auto=format&fit=crop",
            title = "Streetwear urbano",
            rating = "4.6",
            reviewCount = 145,
            comments = "30",
            description = "Sudadera oversize combinada con joggers técnicos. La comodidad no está reñida con el estilo en la ciudad.",
            tags = listOf("streetwear", "comodidad", "urban"),
            category = "PRENDA",
            categoryColor = RedInferno
        ),
        PostItem(
            id = "6",
            creatorName = "Sofia",
            timeAgo = "8 horas",
            avatarUrl = "https://images.unsplash.com/photo-1488423191181-016ce2632205?q=80&w=200&h=200&auto=format&fit=crop",
            postImageUrl = "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?q=80&w=600&auto=format&fit=crop",
            title = "Colección Primavera",
            rating = "4.4",
            reviewCount = 78,
            comments = "22",
            description = "Estampados florales y colores vibrantes para recibir la nueva estación. Tejidos transpirables y cortes fluidos.",
            tags = listOf("primavera", "floral", "boho"),
            category = "PRENDA",
            categoryColor = RedInferno
        )
    )
}
