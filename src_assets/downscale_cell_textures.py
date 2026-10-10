#!/usr/bin/env python3
"""Downscale ICBM's 32px cell textures to 16px without damaging animations.

Each animation frame is cropped before resizing so filtering cannot blend adjacent
frames. A box filter retains thin diagonal features, and a restrained RGB-only
unsharp pass restores the crispness expected of Minecraft item art. Alpha is then
thresholded back to the source art's binary transparency; this prevents translucent
halos on the border and solid end caps while retaining narrow diagonal pixels.

Requires ImageMagick 7 (the ``magick`` command).
"""

from __future__ import annotations

import shutil
import subprocess
import tempfile
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
TEXTURE_DIR = ROOT / "src/main/resources/assets/icbm/textures/item"
TEXTURES = (
    "empty_cell.png",
    "antimatter_cell.png",
    "antimatter_cell_concentrated.png",
    "redmatter_cell.png",
)
SOURCE_FRAME_SIZE = 32
TARGET_FRAME_SIZE = 16


def run(*args: object) -> None:
    subprocess.run([str(arg) for arg in args], check=True)


def dimensions(path: Path) -> tuple[int, int]:
    result = subprocess.run(
        ["magick", "identify", "-format", "%w %h", str(path)],
        check=True,
        capture_output=True,
        text=True,
    )
    width, height = map(int, result.stdout.split())
    return width, height


def downscale(path: Path) -> None:
    width, height = dimensions(path)
    if width != SOURCE_FRAME_SIZE or height % SOURCE_FRAME_SIZE:
        raise ValueError(
            f"{path}: expected a {SOURCE_FRAME_SIZE}px-wide vertical frame strip, "
            f"got {width}x{height}"
        )

    frame_count = height // SOURCE_FRAME_SIZE
    with tempfile.TemporaryDirectory(prefix="icbm-cell-textures-") as temp_name:
        temp_dir = Path(temp_name)
        frames: list[Path] = []
        for index in range(frame_count):
            frame = temp_dir / f"frame-{index:03}.png"
            run(
                "magick",
                path,
                "-crop",
                f"{SOURCE_FRAME_SIZE}x{SOURCE_FRAME_SIZE}+0+{index * SOURCE_FRAME_SIZE}",
                "+repage",
                "-filter",
                "box",
                "-resize",
                f"{TARGET_FRAME_SIZE}x{TARGET_FRAME_SIZE}!",
                "-channel",
                "RGB",
                "-unsharp",
                "0x0.7+0.8+0.02",
                "+channel",
                "-channel",
                "A",
                "-threshold",
                "10%",
                "+channel",
                "-depth",
                "8",
                frame,
            )
            frames.append(frame)

        output = temp_dir / path.name
        run("magick", *frames, "-append", "-depth", "8", output)
        shutil.copyfile(output, path)
        print(f"{path.relative_to(ROOT)}: {frame_count} frame(s) -> {TARGET_FRAME_SIZE}x{frame_count * TARGET_FRAME_SIZE}")


def main() -> None:
    if shutil.which("magick") is None:
        raise SystemExit("ImageMagick 7 is required (missing 'magick' command)")
    for texture_name in TEXTURES:
        downscale(TEXTURE_DIR / texture_name)


if __name__ == "__main__":
    main()
