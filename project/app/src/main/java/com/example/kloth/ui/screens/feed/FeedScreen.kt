package com.example.kloth.ui.screens.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothSecondary
import com.example.kloth.ui.theme.RedInferno

data class PostItem(
    val creatorName: String,
    val timeAgo: String,
    val avatarUrl: String,
    val postImageUrl: String,
    val title: String,
    val rating: String,
    val reviewCount: Int,
    val comments: String,
    val description: String,
    val tags: List<String>,
    val category: String,
    val categoryColor: Color
)

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier
) {
    val mockPosts = listOf(
        PostItem(
            creatorName = "Isabela",
            timeAgo = "2 hours ago",
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBMyCAVr9pWrd_sDsyfrx2v7B_wwZXc4En_Hj06fTPAnAoiyn85C9FsH8p6U6PiT3-zoF2ygVqjkcaxTd2qvb0dJco59T-gId4rhTYZRrypPBw7D21cyI8jziwbOHXhi6RvASwrKdMXNtFM4PZ4vmw0j-UQYyz-l9jQO_PKjkApgQurneLQYUD05xXGaJiFbJ7J7lnhsLCcMpV1reEReT6SL0W_OBqa0a_7S4P7IwHGSYytO0DdrGagg",
            postImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDpW3hTQoK-jwp_QytcmHQOJubOsBnRcVsFRLEcNgPdJCKBG-Az5EM7-DRqTigUiaQ6z8HyWpQaHZUwssvqN8KO_vvC8raEh0FUbOpUv2AXfPTNpcyQktFe7wSFGEyhbhuVrHh0loG-el0Ziv7BFu3YgwsjMTEzkEsjjHKuEE4VW43CRnM6mepoinTZokHM9SxVv_FfSXm_48q454nLpntqlr6ieAxDR6VBbjzF9cH3h3UimDgRUF9Tng",
            title = "Suit elegante negro",
            rating = "4.8",
            reviewCount = 124,
            comments = "42",
            description = "Este traje define la elegancia y minimalismo con sus cortes",
            tags = listOf("minimalista", "otoño", "black outfits"),
            category = stringResource(R.string.badge_couture),
            categoryColor = RedInferno
        ),
        PostItem(
            creatorName = "Mary Luz",
            timeAgo = "5 hours ago",
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC95eWrmdTsvGcWV1NOUN0MzDh83ydDTJqbvYHSRent__IorafJzU75Ekw0imOsmu5YbxUD-D0qrfUPEiAoELNQVHAX2SfPPaZ6dkXRauvVK2mFKBibQgRBCGlVs1xNmHyT5S18k_aVV0-Up-7GWyhZlRS67xh60NH6BFRtcs245CfgwFdlKZqwEBb4mAyhh4eWgoCTcw0VWdfN06gzCnR0W5LMsyphOFYFDv884Cdk6Ld-Gr60z9QFdA",
            postImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB1KG55WXpe9UvmdGLd27HVJIBZfnEGtrsbetkZBgmDeqtGIgk2bHncKagU1Tb-cieQm3z8oZr_I-EU3NWzPzaiTVoHtLBbr8jeqaxaYeLxIYTqhbNk_cJfl6G_zqDE-DDh_7JXvS7WOV-SO_zEcI9nAi2iph5yKUMIfrHv3eTG5bAtNiZHgnDbYu6iB_7oSDlG59oFSFV0ejX5e3_F6W-tQp4H7YBAK-Y3JVNtQpE5fhWSwvWi-Y9rnw",
            title = "Vestido blanco largo",
            rating = "4.5",
            reviewCount = 89,
            comments = "18",
            description = "Este vestido es la muestra de que no se necesita algo extravagante para brillar",
            tags = listOf("minimalista", "summer", "timeless"),
            category = stringResource(R.string.badge_essentials),
            categoryColor = KlothSecondary
        )
    )
    FeedScreenContent(modifier,mockPosts)
}
