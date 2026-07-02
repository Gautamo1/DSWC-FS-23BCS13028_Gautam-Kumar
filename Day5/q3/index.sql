CREATE INDEX idx_holdings_investor_asset_value
ON holdings(investor_id, asset_class, current_value);