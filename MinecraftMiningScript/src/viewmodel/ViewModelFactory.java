package viewmodel;

import model.Model;
import view.AutoFishingViewController;

public class ViewModelFactory
{
  private MinecraftMiningScriptsViewModel minecraftMiningScriptsViewModel;
  private AutoClickViewModel autoclickViewModel;
  private AutoFishingViewModel autoFishingViewModel;
  private AutoMineViewModel autoMineViewModel;

  public ViewModelFactory(Model model)
  {
    this.minecraftMiningScriptsViewModel = new MinecraftMiningScriptsViewModel(
        model);
    this.autoclickViewModel = new AutoClickViewModel(model);
    this.autoFishingViewModel = new AutoFishingViewModel(model);
    this.autoMineViewModel = new AutoMineViewModel(model);
  }

  public MinecraftMiningScriptsViewModel getMinecraftMiningScriptsViewModel()
  {
    return minecraftMiningScriptsViewModel;
  }

  public AutoClickViewModel getAutoclickViewModel()
  {
    return autoclickViewModel;
  }

  public AutoFishingViewModel getAutoFishingViewModel()
  {
    return autoFishingViewModel;
  }

  public AutoMineViewModel getAutoMineViewModel()
  {
    return autoMineViewModel;
  }
}
