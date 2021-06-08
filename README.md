[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b44027b9cc04465fbe893e92c7c9164a)](https://www.codacy.com/gh/Freezlex/JamesBot/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Freezlex/questter&amp;utm_campaign=Badge_Grade)
[![Java CI with Gradle](https://github.com/Freezlex/JamesBot/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/Freezlex/JamesBot/actions/workflows/gradle.yml)
[![Discord Bots Status](https://top.gg/api/widget/status/425377070525317120.svg)](https://top.gg/bot/425377070525317120)
[![Discord Bots Owner](https://top.gg/api/widget/owner/425377070525317120.svg)](https://top.gg/bot/425377070525317120)


## JamesBot - Kotlin

UNDER DEVELOPPEMENT

## Commits identity *based on [Emoji-Log](https://github.com/ahmadawais/Emoji-Log)*

For better understanding of development we are using commit identity. In fact all of our commit start with an 
emoji and a snippet, it help to track the main purpose of a commit.

| Keyword |   Snippet    |
| ------- | ------------ |
| `new` | 📦 NEW      |
| `imp` | 👌 IMPROVE  |
| `fix` | 🐛 FIX      |
| `rlz` | 🚀 RELEASE  |
| `doc` | 📖 DOC      |
| `tst` | 🤖 TEST     |
| `brk` | ‼️ BREAKING  |

### How to use :


To sign your commit use `git <keyword> "<your message>"`.
Only use the following Git Commit Messages. A simple and small footprint is critical here.

1. `📦 NEW: IMPERATIVE_MESSAGE_GOES_HERE`
   > Use when you add something entirely new.
   > E.g. `📦 NEW: Add Git ignore file`

1. `👌 IMPROVE: IMPERATIVE_MESSAGE_GOES_HERE`
   > Use when you improve/enhance piece of code like refactoring etc.
   > E.g. `👌 IMPROVE: Remote IP API Function`

1. `🐛 FIX: IMPERATIVE_MESSAGE_GOES_HERE`
   > Use when you fix a bug — need I say more?
   > E.g. `🐛 FIX: Case conversion`

1. `📖 DOC: IMPERATIVE_MESSAGE_GOES_HERE`
   > Use when you add documentation like `README.md`, or even inline docs.
   > E.g. `📖 DOC: API Interface Tutorial`


1. `🚀 RELEASE: IMPERATIVE_MESSAGE_GOES_HERE`
   > Use when you release a new version.
   > E.g. `🚀 RELEASE: Version 2.0.0`


1. `🤖 TEST: IMPERATIVE_MESSAGE_GOES_HERE`
   > Use when it's related to testing.
   > E.g. `🤖 TEST: Mock User Login/Logout`


1. `‼️ BREAKING: IMPERATIVE_MESSAGE_GOES_HERE`
   > Use when releasing a change that breaks previous versions.
   > E.g. `‼️ BREAKING: Change authentication protocol`

Add this configuration into your `~/.gitconfig` :

```shell
# Make sure you're adding under the [alias] block.
[alias]
  # Git Commit, Add all and Push — in one step.
  ca = "!f() { git add .; git commit -m \"$@\"; }; f"
  # NEW.
  new = "!f() { git ca \"📦\\`new\\` : $@\"; }; f"
  # IMPROVE.
  imp = "!f() { git ca \"👌\\`improve\\` : | $@\"; }; f"
  # FIX.
  fix = "!f() { git ca \"🐛\\`fix\\` : $@\"; }; f"
  # RELEASE.
  rlz = "!f() { git ca \"🚀\\`release\\` : $@\"; }; f"
  # DOC.
  doc = "!f() { git ca \"📖\\`doc\\` : $@\"; }; f"
  # TEST.
  tst = "!f() { git ca \"🤖\\`test\\`: $@\"; }; f"
  # BREAKING CHANGE.
  brk = "!f() { git ca \"‼️\\`breaking\\` : $@\"; }; f"
```

## Using our code

**Give credit where credit is due.** If you wish to use our code in a project, please credit us, and take your time to read our full license. We don't mind you using JamesBot code, as it is open-source for a reason, as long as you don't blatantly copy it or refrain from crediting us. Take special care of following the license aswell.

## License

Copyright (C) 2021  Freezlex

> This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
>
> This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
>
>See the
GNU General Public License for more details.
> You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
