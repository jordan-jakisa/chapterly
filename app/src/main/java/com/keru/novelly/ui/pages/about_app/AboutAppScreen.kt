package com.keru.novelly.ui.pages.about_app

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.LocalPolice
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keru.novelly.R
import com.keru.novelly.ui.components.SettingsCard
import com.keru.novelly.utils.openLink
import com.keru.novelly.utils.rateApp
import com.keru.novelly.utils.sendEmail
import com.keru.novelly.utils.toastComingSoon

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AboutAppScreen(navController: NavController = rememberNavController()) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "About") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )

                }
            }
        )
    })
    { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 72.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, bottom = 16.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(top = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Designed and developed by", fontSize = 12.sp)
                        Text(
                            text = "Jordan Mungujakisa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            IconButton(onClick = { context.openLink("https://www.linkedin.com/in/jordanmungujakisa/") }) {

                                Icon(
                                    painter = painterResource(R.drawable.linkedin),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(onClick = { context.openLink("https://twitter.com/JakisaJordan") }) {

                                Icon(
                                    painter = painterResource(R.drawable.twitter_alt),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(onClick = { context.openLink("https://medium.com/@jordan-mungujakisa") }) {

                                Icon(
                                    painter = painterResource(R.drawable.medium),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp)

                                )
                            }
                            IconButton(onClick = { context.openLink("https://www.fiverr.com/jordan_jakisa") }) {
                                Icon(
                                    painter = painterResource(R.drawable.fiverr),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                        }

                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.luffy),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(50)
                        )
                        .size(120.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )
            }
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingsCard(
                        title = "Changelog",
                        icon = Icons.Rounded.TrackChanges
                    ) {
                        context.toastComingSoon()
                    }
                    SettingsCard(title = "Donate", icon = Icons.Rounded.CardGiftcard) {
                        context.toastComingSoon()
                    }
                    SettingsCard(
                        title = "Feedback",
                        icon = Icons.Rounded.MailOutline
                    ) {
                        context.sendEmail(
                            subject = "App Feedback",
                            ""
                        )
                    }
                    SettingsCard(
                        title = "Rate App",
                        icon = Icons.Rounded.StarRate
                    ) {
                        context.rateApp()
                    }
                    SettingsCard(title = "Open source licenses", icon = Icons.Rounded.LocalPolice) {
                        val intent = Intent(context, OssLicensesMenuActivity::class.java)
                        startActivity(context, intent, null)
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Support Authors", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "If you like a book, please take the time to leave a nice review on Amazon, " +
                                "Goodreads etc. This will support the author indirectly by nudging someone to buy " +
                                "his book. This is the least you can do after reading a free copy.",
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                text = "Made with ❤️ from Uganda and teh world!",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}