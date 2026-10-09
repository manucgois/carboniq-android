package br.com.fiap.carboniq.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.carboniq.R
import br.com.fiap.carboniq.ui.AirQualityState
import br.com.fiap.carboniq.ui.theme.CarbonIQTheme

@Composable
fun HomeScreen(
    totalCo2: Float,
    airQuality: AirQualityState,
    onRetryAirQuality: () -> Unit,
    onNewSimulationClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onTipsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(24.dp))
        SummaryCard(totalCo2 = totalCo2)
        Spacer(modifier = Modifier.height(16.dp))
        AirQualityCard(state = airQuality, onRetry = onRetryAirQuality)
        Spacer(modifier = Modifier.height(24.dp))
        ActionButtons(
            onNewSimulationClick = onNewSimulationClick,
            onHistoryClick = onHistoryClick,
            onTipsClick = onTipsClick
        )
    }
}

@Composable
private fun HeaderSection() {
    Column {
        Image(
            painter = painterResource(R.drawable.logo_reduzida),
            contentDescription = "Logo reduzida CarbonIQ",
            modifier = Modifier.height(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Olá!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Vamos calcular o seu impacto hoje?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Image(
                painter = painterResource(R.drawable.planetaterra),
                contentDescription = "Planeta terra",
                modifier = Modifier.height(50.dp)
            )
        }
    }
}

@Composable
private fun SummaryCard(totalCo2: Float) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.carbon_footprint),
                    contentDescription = "Pegada de gás carbono",
                    modifier = Modifier.height(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Seu impacto",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "TOTAL",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = String.format(java.util.Locale("pt", "BR"), "%.1f", totalCo2),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = "kg CO₂e",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer)

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "emitidos este mês",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(R.drawable.leaf),
                    contentDescription = "Ícone folha",
                    modifier = Modifier.height(30.dp)
                )
            }
        }
    }
}

private fun aqiLabel(aqi: Int): String = when {
    aqi <= 20 -> "Boa"
    aqi <= 40 -> "Razoável"
    aqi <= 60 -> "Moderada"
    aqi <= 80 -> "Ruim"
    aqi <= 100 -> "Muito ruim"
    else -> "Extremamente ruim"
}

@Composable
private fun AirQualityCard(
    state: AirQualityState,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Qualidade do ar agora",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (state) {
                is AirQualityState.Loading -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Carregando...",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                is AirQualityState.Success -> {
                    val aqi = state.data.europeanAqi
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Índice: $aqi",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${aqiLabel(aqi)})",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "PM2.5: ${state.data.pm25} µg/m³",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                is AirQualityState.Error -> {
                    Text(
                        text = "Não foi possível carregar os dados.",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TextButton(onClick = onRetry) {
                        Text("Tentar novamente")
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onNewSimulationClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onTipsClick: () -> Unit
) {
    Column {
        Button(
            onClick = onNewSimulationClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Nova simulação",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onHistoryClick,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Histórico")
            }

            OutlinedButton(
                onClick = onTipsClick,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Dicas")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenPreview() {
    CarbonIQTheme {
        HomeScreen(
            totalCo2 = 128.4f,
            airQuality = AirQualityState.Success(
                br.com.fiap.carboniq.data.CurrentAirQuality(
                    time = "",
                    europeanAqi = 32,
                    pm25 = 12.3
                )
            ),
            onRetryAirQuality = {},
            onNewSimulationClick = {},
            onHistoryClick = {},
            onTipsClick = {}
        )
    }
}