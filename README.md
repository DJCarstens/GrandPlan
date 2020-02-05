# GrandPlan
A time management and planning web app.

## Members
- [Samantha Hillebrand](https://github.com/sammyjh30) - sammyjh30@gmail.com
- [Dylan Carstens](https://github.com/skwigi) - dyljoucars@gmail.com
- [Chelsea Jessiman](https://github.com/chelseajessiman) - chelseajessiman1998@gmail.com
- [Ethan Lindeman](https://github.com/plethargy) - ethan.lindeman@gmail.com
- [Emma Coetzer](https://github.com/EmmaBinx) - emmabinx18@gmail.com

## Compilation

## How to contribute

Checkout the `dev` branch:

```
  git checkout dev
```

Create a new feature branch. Name what the branch is for (feature/defect/etc.), name the ticket number (GG-123), and finally give a descriptive name in lowercase and seperated by hyphens:

```
  git checkout -b feature/GG-123-my-fancy-feature
```

Write all of your code in this branch, and once you're done, merge the dev
branch:

```
  git checkout dev
  git pull
  git checkout feature/GG-123-my-fancy-feature
  git merge dev
```

Then push your branch upstream:

```
  git push -u origin feature/GG-123-my-fancy-feature
```

And finally, submit a pull request from `my-fancy-feature` to `dev` by going [here](https://github.com/DJCarstens/GrandPlan/pulls).

Another member of the team will then review your pull request and merge it into
dev.
