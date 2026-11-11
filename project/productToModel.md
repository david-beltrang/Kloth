Product to ModelPowered by best-in-class image editing AI, the Product to Model endpoint transforms product images into people wearing those products, with optional guidance from an inspiration image, background, or face reference.
This endpoint is designed specifically for wearable fashion items such as clothing, shoes, hats, jewelry, bags, and accessories.
Lifecycle: Preview · Released: September 2, 2025
Combining a product with an existing model imageTo place a product on an existing person image while preserving that image's
pose and background, use the tryon-max endpoint
with model_image. On Product to Model, a model's identity is passed via
face_reference instead, and the endpoint generates a new image of that
person wearing the product; it does not replace the clothing or keep the pose
and background of the reference photo.
Request body
Every call uses the universal POST /v1/run request body:
FieldTypeRequiredDescriptionmodel_namestringYesMust be product-to-model.inputsobjectYesContains the endpoint-specific parameters documented under Input parameters.
Place all endpoint parameters inside inputs; they are not top-level request fields.
Request
Generate product-to-model images by submitting your product image to the universal /v1/run endpoint:
POSThttps://api.fashn.ai/v1/runRequest examplescURLJavaScriptPythoncurl -X POST https://api.fashn.ai/v1/run \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_API_KEY" \
     -d '{
           "model_name": "product-to-model",
           "inputs": {
             "product_image": "http://example.com/path/to/product.jpg",
             "prompt": "professional office setting",
             "output_format": "png",
             "return_base64": false
           }
         }'Response200Returns a prediction ID for status polling:Response{
  "id": "123a87r9-4129-4bb3-be18-9c9fb5bd7fc1-u1",
  "error": null
}
Input parameters
product_imageRequiredimage URL | data URIProduct image to be worn. Supports clothing, accessories, shoes, and other
wearable fashion items.
image_promptimage URL | data URIOptional inspiration image that guides pose, environment, and lighting while keeping the product centered in the final output.Default: Not set
face_referenceimage URL | data URIOptional face identity reference to guide who the generated person should look like. When provided, the pipeline refines identity to match the reference while keeping product fidelity. Using a face reference typically adds around 20 seconds to the processing time.Default: Not set
face_reference_mode'match_base' | 'match_reference'Controls how the identity from face_reference influences pose and expression.-match_reference favors the reference face’s pose and expression for maximum resemblance.-match_base gives more weight to the prompt (or system default prompt if omitted) when generating the person's pose and expression.Default: match_reference
promptstringAdditional instructions for person appearance, styling preferences, or background.Examples: "man with tattoos", "tucked-in", "open jacket", "rolled-up sleeves", "studio background".Default: Not set
aspect_ratiostringDesired aspect ratio for the output image. If omitted, the generation inherits the aspect ratio from the most specific image supplied (background_reference → image_prompt → product_image). Provide an explicit ratio to override that default even when using these image references.Supported ratios: "1:1", "3:4", "4:3", "9:16", "16:9", "2:3", "3:2", "4:5", "5:4"Default: Aspect ratio of the most specific image supplied
resolution'1k' | '2k' | '4k'Output resolution tier. '1k' produces ~1 megapixel output, '2k' ~4 megapixels, and '4k' ~16 megapixels. Exact output dimensions depend on this tier and the image aspect ratio.Default: 1k
generation_mode'fast' | 'balanced' | 'quality'Sets the generation quality level. 'quality' produces the most detailed and
realistic output but takes longer to process and costs more credits. 'fast'
prioritizes speed and lower cost. If omitted, FASHN selects generation_mode
automatically. For product-to-model, omitted generation_mode is currently
billed as 'fast' at 1k and as 'balanced' at 2k or 4k.
background_referenceimage URL | data URIBackground image used as the backdrop for generation. Ensures location consistency across generations. If a person appears in the image, they will be ignored and only the background will be used.When provided alongside image_prompt, image_prompt governs the model's appearance, pose, and styling, while background_reference anchors the scene.Default: Not set
seedintegerSeed for reproducible results. Must be between 0 and 2^32-1.Default: 42
num_imagesintegerNumber of images to generate in a single request. Must be between 1 and 4. Additional images consume more compute (and credits) and can increase processing time.Default: 1
output_formatstringOutput image format.
"png" - PNG format, original quality
"jpeg" - JPEG format, smaller file size
Default: png
return_base64booleanWhen set to true, the API will return the generated image as a base64-encoded string instead of a CDN URL. The base64 string will be prefixed data:image/png;base64,....Privacy benefit: Outputs are available for up to 60 minutes instead of the standard three-day window. Request history contains only a <base64> placeholder.Default: false
Technical limitations
RequirementLimitImage file size30 MiB per imageMinimum dimensions15 × 15 pxAspect ratio1:16 to 16:1
Inputs outside these limits fail with InputValidationError. The output resolution does not change the input limits.
Credit cost
generation_mode \ resolution1k2k4kfast123balanced234quality345
Additional pricing rules:

face_reference adds +3 credits per output image.
num_images multiplies the total cost by the number of outputs requested.
If generation_mode is omitted, automatic pricing applies.

Processing time
Processing time depends on both resolution and generation_mode. The fastest configuration (fast + 1k) typically completes in around 10 seconds, balanced at 2k finishes in around 25 seconds, and the most intensive combination (quality + 4k) typically takes around 55 seconds. Using face_reference typically adds around 20 seconds on top of any configuration. Actual latency may vary with current server load.
Response polling
After submitting your request, poll the status endpoint using the returned prediction ID. See API Fundamentals for complete polling details.
Successful response
When your product-to-model generation completes successfully, the status endpoint will return:
{
  "id": "123a87r9-4129-4bb3-be18-9c9fb5bd7fc1-u1",
  "status": "completed",
  "output": [
    "https://cdn.fashn.ai/123a87r9-4129-4bb3-be18-9c9fb5bd7fc1-u1/output_0.png"
  ],
  "error": null
}
Runtime errors
Runtime errors for Product to Model use the shared set in Error Handling.
Related guides

Prompting in FASHN ↗ - Learn how to write effective prompts for best results
Image Preprocessing Best Practices - Optimize your input images for better results
Data Retention & Privacy - Understand how FASHN handles your data
