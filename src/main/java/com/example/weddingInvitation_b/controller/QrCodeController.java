package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * QR 코드 API 컨트롤러
 *
 * <p>청첩장 URL 기반으로 QR 코드 이미지(PNG)를 생성하여 반환한다.
 * ZXing 라이브러리를 사용하며 응답은 image/png 바이너리이다.</p>
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class QrCodeController {

    private final McardRepository mcardRepository;

    @Value("${app.base-url:https://mcard.example.com}")
    private String baseUrl;

    private static final int QR_SIZE = 300;

    /**
     * QR 코드 이미지 생성 및 반환
     *
     * <p>청첩장의 초대 코드로 공개 URL을 생성하고 QR 코드 PNG 이미지를 반환한다.
     * 응답 Content-Type은 image/png이며 즉시 다운로드 가능하다.</p>
     *
     * @param mcardId 청첩장 ID
     * @return QR 코드 PNG 이미지 바이트
     * @throws EntityNotFoundException 청첩장이 없을 경우
     */
    @GetMapping("/{mcardId}/qrcode")
    public ResponseEntity<byte[]> getQrCode(@PathVariable Long mcardId) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        String inviteUrl = baseUrl + "/w/" + mcard.getInviteCode();

        try {
            byte[] qrBytes = generateQrPng(inviteUrl, QR_SIZE);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "qrcode-" + mcard.getInviteCode() + ".png");
            headers.setContentLength(qrBytes.length);

            return ResponseEntity.ok().headers(headers).body(qrBytes);

        } catch (WriterException | IOException e) {
            throw new RuntimeException("QR 코드 생성에 실패했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 주어진 텍스트로 QR 코드 PNG 바이트 배열을 생성한다.
     *
     * @param text    QR 코드에 인코딩할 텍스트 (URL)
     * @param size    QR 코드 이미지 크기 (픽셀)
     * @return PNG 이미지 바이트 배열
     * @throws WriterException QR 코드 생성 오류
     * @throws IOException     이미지 스트림 오류
     */
    private byte[] generateQrPng(String text, int size) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = Map.of(
            EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M,
            EncodeHintType.CHARACTER_SET, "UTF-8",
            EncodeHintType.MARGIN, 1
        );

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}
