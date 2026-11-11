Try-On MaxTry-On Max is our recommended virtual try-on endpoint, built for AI fashion photoshoots and publishable e-commerce content. It places products onto model images with enhanced fidelity, producing images suitable for PDPs, catalogs, and marketing assets.
This endpoint supports clothing, shoes, hats, jewelry, bags, and other wearable fashion items.
Lifecycle: Preview · Released: January 26, 2026
Request body
Every call uses the universal POST /v1/run request body:
FieldTypeRequiredDescriptionmodel_namestringYesMust be tryon-max.inputsobjectYesContains the endpoint-specific parameters documented under Input parameters.
Place all endpoint parameters inside inputs; they are not top-level request fields.
Request
Generate try-on images by submitting your product and model images to the universal /v1/run endpoint:
POSThttps://api.fashn.ai/v1/runRequest examplescURLJavaScriptPythoncurl -X POST https://api.fashn.ai/v1/run \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_API_KEY" \
     -d '{
           "model_name": "tryon-max",
           "inputs": {
             "product_image": "https://example.com/garment.jpg",
             "model_image": "https://example.com/person.jpg"
           }
         }'Response200Returns a prediction ID for status polling:Response{
  "id": "123a87r9-4129-4bb3-be18-9c9fb5bd7fc1-u1",
  "error": null
}
Input parameters
product_imageRequiredimage URL | data URIProduct image (garment, accessory, etc.) to place on the model.
model_imageRequiredimage URL | data URIImage of the person who will wear the product. The try-on process preserves
the model's identity, pose, and styling while seamlessly integrating the
product.
promptstringOptional instructions to customize the try-on result. Use this to adjust how the product is worn or make minor styling changes.Examples: "remove scarf", "tuck in shirt", "roll up sleeves", "open jacket".Default: Empty string (uses intelligent defaults based on the images)
resolution'1k' | '2k' | '4k'Output resolution tier. '1k' produces ~1 megapixel output, '2k' ~4 megapixels, and '4k' ~16 megapixels. Exact output dimensions depend on this tier and the image aspect ratio.Default: 1k
generation_mode'fast' | 'balanced' | 'quality'Sets the generation quality level. 'quality' produces the most detailed and
realistic output but takes longer to process and costs more credits.
'balanced' offers a middle ground between speed and quality. 'fast'
prioritizes speed and lower cost. If omitted, FASHN selects generation_mode
automatically. For tryon-max, omitted generation_mode is currently billed
as 'balanced' at all resolutions.
seedintegerSets random operations to a fixed state. Use the same seed to reproduce results with the same inputs, or a different seed to force different results.Default: 42 · Range: 0 to 2^32 - 1
num_imagesintegerNumber of images to generate in a single request. Must be between 1 and 4. Additional images consume more compute (and credits) and can increase processing time.Default: 1
output_format'png' | 'jpeg'Specifies the output image format.
png: Lossless quality, best for further editing or when maximum fidelity is needed.
jpeg: Smaller file size with slight compression, suitable for direct web use.
Default: png
return_base64booleanWhen set to true, the API will return the generated image as a base64-encoded string instead of a CDN URL. The base64 string will be prefixed according to the output_format (e.g., data:image/png;base64,... or data:image/jpeg;base64,...).Privacy benefit: Outputs are available for up to 60 minutes instead of the standard three-day window. Request history contains only a <base64> placeholder.Default: false
Technical limitations
RequirementLimitImage file size30 MiB per imageMinimum dimensions15 × 15 pxAspect ratio1:16 to 16:1
Inputs outside these limits fail with InputValidationError. The output resolution does not change the input limits.
Credit cost
generation_mode \ resolution1k2k4kfast123balanced234quality345
Additional pricing rules:

num_images multiplies the total cost by the number of outputs requested.
If generation_mode is omitted, automatic pricing applies.

Processing time
Processing time depends on both resolution and generation_mode. The fastest configuration (fast + 1k) typically completes in around 10 seconds, balanced at 2k finishes in around 25 seconds, and the most intensive combination (quality + 4k) typically takes around 55 seconds. Actual latency may vary with current server load.
Response polling
After submitting your request, poll the status endpoint using the returned prediction ID. See API Fundamentals for complete polling details.
Successful response
When your try-on generation completes successfully, the status endpoint will return:
{
  "id": "123a87r9-4129-4bb3-be18-9c9fb5bd7fc1-u1",
  "status": "completed",
  "output": [
    "https://cdn.fashn.ai/123a87r9-4129-4bb3-be18-9c9fb5bd7fc1-u1/output_0.png"
  ],
  "error": null
}
The output array contains URLs to your generated try-on images with the product seamlessly placed on the model.
Runtime errors
Runtime errors for Try-On Max use the shared set in Error Handling.
Related guides

Prompting in FASHN ↗ - Learn how to write effective prompts for best results
Image Preprocessing Best Practices - Optimize your input images for better results
Data Retention & Privacy - Understand how FASHN handles your data
