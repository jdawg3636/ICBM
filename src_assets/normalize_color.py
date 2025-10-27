# Quick-and-dirty ChatGPT Script used to generate the normalized variant of the black hole texture used to enable runtime color change in code.
# This was necessary for the Nightmare Blast, which (currently) creates an Halloween-colored variant of the usual Red Matter black hole.
# Note that the values it prints in "Color Correction Info" seem to have the red and blue channels flipped and even still were a bit off - the
# values actually used in the mod code are from manual trial-and-error refinements of the original values produced by this script.
# Run using Python 3.11.2
# Requires OpenCV: pip install opencv-contrib-python

import cv2
import numpy as np

# Load image with alpha preserved
img = cv2.imread("redmatter_blast_accretion_disk.png", cv2.IMREAD_UNCHANGED).astype(np.float32) / 255.0

# Separate RGB and alpha
if img.shape[2] == 4:
    rgb = img[:, :, :3]
    alpha = img[:, :, 3:]
else:
    rgb = img
    alpha = None

# Only consider visible pixels (alpha > 0.01)
if alpha is not None:
    mask = (alpha > 0.01).astype(np.float32)
    weighted_mean = (rgb * mask).sum(axis=(0, 1)) / mask.sum()
else:
    weighted_mean = rgb.mean(axis=(0, 1))

# Compute scale to normalize color balance
scale = weighted_mean.max() / weighted_mean
rgb_corrected = np.clip(rgb * scale, 0, 1)

# Recombine alpha
if alpha is not None:
    img_corrected = np.concatenate([rgb_corrected, alpha], axis=2)
else:
    img_corrected = rgb_corrected

# Save output
cv2.imwrite("accretion_disk.png", (img_corrected * 255).astype(np.uint8))

# Compute inverse multipliers to restore original look at runtime
runtime_restore = 1.0 / scale
runtime_restore /= runtime_restore.max()  # normalize so max = 1

print("---- Color Correction Info ----")
print(f"Original mean color per channel (R,G,B): {weighted_mean}")
print(f"Applied neutralization scale (S): {scale}")
print(f"Use these multipliers in code to restore original color:")
print(f"R: {runtime_restore[0]:.3f}, G: {runtime_restore[1]:.3f}, B: {runtime_restore[2]:.3f}")
