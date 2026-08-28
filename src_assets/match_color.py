#!/usr/bin/env python3
"""Match one texture's colors to a reference texture while preserving alpha.

Requires OpenCV and NumPy. From the repository root:
    .venv/bin/python src_assets/normalize_color.py SOURCE REFERENCE -o OUTPUT
"""

from __future__ import annotations

import argparse
import os
from pathlib import Path

import cv2
import numpy as np


def load_texture(path: Path) -> tuple[np.ndarray, np.ndarray | None]:
    image = cv2.imread(str(path), cv2.IMREAD_UNCHANGED)
    if image is None:
        raise ValueError(f"Could not read PNG: {path}")

    if image.ndim == 2:
        return cv2.cvtColor(image, cv2.COLOR_GRAY2BGR), None
    if image.shape[2] == 4:
        return image[:, :, :3], image[:, :, 3]
    if image.shape[2] == 3:
        return image, None
    raise ValueError(f"Unsupported channel count in {path}: {image.shape[2]}")


def visible_pixels(lab: np.ndarray, alpha: np.ndarray | None) -> np.ndarray:
    if alpha is None:
        return lab.reshape(-1, 3)
    pixels = lab[alpha > 0]
    if pixels.size == 0:
        raise ValueError("Texture has no visible pixels")
    return pixels


def match_colors(
    source_bgr: np.ndarray,
    source_alpha: np.ndarray | None,
    reference_bgr: np.ndarray,
    reference_alpha: np.ndarray | None,
) -> np.ndarray:
    """Transfer the reference's Lab mean and contrast to the source."""
    source_lab = cv2.cvtColor(source_bgr, cv2.COLOR_BGR2LAB).astype(np.float32)
    reference_lab = cv2.cvtColor(reference_bgr, cv2.COLOR_BGR2LAB).astype(np.float32)

    source_pixels = visible_pixels(source_lab, source_alpha)
    reference_pixels = visible_pixels(reference_lab, reference_alpha)
    source_mean = source_pixels.mean(axis=0)
    reference_mean = reference_pixels.mean(axis=0)
    source_std = source_pixels.std(axis=0)
    reference_std = reference_pixels.std(axis=0)
    scale = np.divide(
        reference_std,
        source_std,
        out=np.ones(3, dtype=np.float32),
        where=source_std > 1e-6,
    )

    matched_lab = (source_lab - source_mean) * scale + reference_mean
    matched_lab = np.clip(matched_lab, 0, 255).astype(np.uint8)
    matched_bgr = cv2.cvtColor(matched_lab, cv2.COLOR_LAB2BGR)

    if source_alpha is None:
        return matched_bgr
    return np.dstack((matched_bgr, source_alpha))


def default_output(source: Path) -> Path:
    return source.with_name(f"{source.stem}_color_matched{source.suffix}")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path, help="Texture whose colors will change")
    parser.add_argument("reference", type=Path, help="Texture whose colors will be matched")
    parser.add_argument("-o", "--output", type=Path, help="Output PNG (defaults beside source)")
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    output = args.output or default_output(args.source)
    source_bgr, source_alpha = load_texture(args.source)
    reference_bgr, reference_alpha = load_texture(args.reference)
    matched = match_colors(source_bgr, source_alpha, reference_bgr, reference_alpha)

    output.parent.mkdir(parents=True, exist_ok=True)
    temporary = output.with_name(f".{output.stem}.tmp{output.suffix}")
    if not cv2.imwrite(str(temporary), matched):
        raise OSError(f"Could not write PNG: {temporary}")
    os.replace(temporary, output)
    print(f"Wrote color-matched texture to {output}")


if __name__ == "__main__":
    main()
