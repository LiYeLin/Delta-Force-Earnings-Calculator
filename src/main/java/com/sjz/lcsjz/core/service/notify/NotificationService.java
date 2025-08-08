package com.sjz.lcsjz.core.service.notify;

import com.sjz.lcsjz.core.service.model.AnalyzeRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {

    private static final String apiUrlTemplate = "https://sctapi.ftqq.com/%s.send";
    @Resource
    private RestTemplate restTemplate;
    // 从application.yml中注入配置
    @Value("${notification.server-chan.send-key}")
    private String sendKey;

    /**
     * 将分析出的信号列表发送到Server酱
     *
     * @param signals 信号列表 (已过滤掉HOLD信号)
     */
    public void sendSignals(List<AnalyzeRecord> signals) {
        if (signals == null || signals.isEmpty()) {
            log.info("【通知服务】无有效信号，本次不发送通知。");
            return;
        }

        // 1. 构造消息标题和内容
        String title = String.format("发现 %d 个新交易信号", signals.size());
        String desp = formatSignalsToMarkdown(signals);

        // 2. 准备HTTP POST请求
        String url = String.format(apiUrlTemplate, sendKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("title", title);
        body.add("desp", desp);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 3. 发送请求并记录结果
        try {
            log.info("【通知服务】准备发送 {} 个信号到Server酱...", signals.size());
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("【通知服务】发送成功, 响应: {}", response.getBody());
            } else {
                log.error("【通知服务】发送失败, 状态码: {}, 响应: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("【通知服务】调用Server酱API时发生异常", e);
        }
    }

    /**
     * 将信号列表格式化为美观的Markdown文本
     */
    private String formatSignalsToMarkdown(List<AnalyzeRecord> signals) {
        return signals.stream()
                .map(record -> {
                    String signalEmoji = "BUY".equals(record.signal().name()) ? "📈" : "📉";
                    String title = String.format("### %s %s: %s", signalEmoji, record.signal().name(), record.item().getItemName());
                    String priceInfo = String.format("- **当前价格**: %.2f", record.context().currentPrice());
                    String rsiInfo = String.format("- **rsi**: %s", record.context().rsi());
                    String lowerInfo = String.format("- **布林带下轨**: %s", record.context().bollingerLowerBand());
                    String upperInfo = String.format("- **布林带上轨**: %s", record.context().bollingerUpperBand());
                    String goldenCrossInfo = String.format("- **黄金交叉**: %s", record.context().isGoldenCross());
                    String p20Info = String.format("- **P20**: %s", record.context().p20Price());
                    String p80Info = String.format("- **P80**: %s", record.context().p80Price());
                    // 您可以将更多的指标加入到这里，例如RSI, P20等
                    // String rsiInfo = String.format("- **RSI**: %.2f", record.rsi());
                    return title + "\n\n" + priceInfo + "\n\n" + rsiInfo + "\n\n" + lowerInfo + "\n\n" + upperInfo + "\n\n" + goldenCrossInfo + "\n\n" + p20Info + "\n\n" + p80Info;
                })
                .collect(Collectors.joining("\n\n---\n\n")); // 使用 --- 分割不同的信号
    }
}